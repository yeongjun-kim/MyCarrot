package com.mvvm.mycarrot.repository

import android.app.Application
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.util.Log
import android.widget.TextView
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.mvvm.mycarrot.model.ItemObject
import com.mvvm.mycarrot.model.UserObject
import com.mvvm.mycarrot.view.LoginActivity
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.max

class FirebaseRepository private constructor() {

    /**
     *
     * loginMode: LoginActivity에서 startactvity 할 activity를 정하는 모드
     *  0: startActivity X ( 초기 )
     *  1: startActivity SignUp ( FirebaseAuth 만 가입되어있고, Firestore(users)엔 없는상황 )
     *  2: startActivity MainActivity ( FirebaseAuth , Firestore(users) 에 둘다 존재하는 상황 )
     *
     * currentUserObject: 현재 로그인 한 유저에 대한 UserObject
     * location: 현재 로그인 한 유저의 위치
     * lat: 현재 로그인 한 유저의 latitude
     * long: 현재 로그인 한 유저의 longitude
     * profileUri: 현재 로그인 한 유저의 profile image uri (firestore)
     * category: WriteActivity 에서 선택한 카테고리
     * extraArrange: 현재 lat, lang 기준 플러스마이너스 위경도 범위
     *
     * isSignSuccess: SigninActivity 에서 observe, 계정등록이 완료되면 true
     * isStartItemActivity: HomeFragment 에서 ItemActivity 로 넘어가기 전, selectedItem/selectedItemOwner 값을 모두 세팅 해야 2
     *
     * homeItemList: HomeFragent 에 보여질 item List
     * homeItemQuery: homeItemList 를 firestore 에서 get 할때 paging에 쓰기위함 (null = 첫페이지, not null = 첫페이지X)
     * selectedItem: HomeFragment 에서 클릭 한 Item (ItemActivity)
     * selectedItemOwner: HomeFragment 에서 클릭 한 Item Owner (ItemActivity)
     * selectedItemOwnersItem: ItemActivity 에서 [더보기] 클릭시 SeemoreViewModel 에서 가져가 SeeMoreActivity 에서 보여줄 List
     */

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseStore = FirebaseFirestore.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()

    var currentUserObject: MutableLiveData<UserObject> = MutableLiveData()
    var loginMode: MutableLiveData<Int> = MutableLiveData(0)
    var location: MutableLiveData<String> = MutableLiveData("관악구 은천동")
    var lat = 37.55
    var long = 126.97
    var profileUrl = ""
    var category: MutableLiveData<String> = MutableLiveData("카테고리 선택")
    var extraArrange: Double = 0.01

    var isSignSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    var isWriteSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    var isStartItemActivity: MutableLiveData<Int> = MutableLiveData(0)

    var homeItemQuery: Query? = null
    var homeItemList: MutableLiveData<List<ItemObject>> = MutableLiveData(listOf())

    var selectedItem = ItemObject()
    var selectedItemOwner = UserObject()
    var selectedItemOwnersItem: List<ItemObject> = listOf()

    var testCount = 0

    companion object {
        @Volatile
        private var instance: FirebaseRepository? = null

        @JvmStatic
        fun getInstance(): FirebaseRepository =
            instance ?: synchronized(this) {
                instance ?: FirebaseRepository().also { instance = it }
            }
    }

    init {
        initCurrentUser()
        loginMode.value = 0
    }


    fun getIsStartItemActivity() = isStartItemActivity
    fun clearIsStartItemActivity() {
        isStartItemActivity.value = 0
    }

    fun getselectedItem() = selectedItem
    fun setSelectedItem(id: String) {
        incrementLookup(id)

        firebaseStore.collection("items")
            .document(id)
            .get()
            .addOnSuccessListener { result ->
                selectedItem = result.toObject(ItemObject::class.java)!!
                isStartItemActivity.value = isStartItemActivity.value!!.plus(1)
            }
    }

    fun getselectedItemOwner() = selectedItemOwner
    fun setSelectedItemOwner(id: String) {
        firebaseStore.collection("users")
            .document(id)
            .get()
            .addOnSuccessListener { result ->
                selectedItemOwner = result.toObject(UserObject::class.java)!!
                isStartItemActivity.value = isStartItemActivity.value!!.plus(1)
            }
    }





    fun addToLikeList(id: String) {
        var docRef = firebaseStore.collection("users").document(currentUserObject.value!!.userId!!)
        docRef.update("likeList", FieldValue.arrayUnion(id))

        updateCurrentUser()
        incrementLikeCount(id)
    }

    fun deleteFromLikeList(id: String) {
        var docRef = firebaseStore.collection("users").document(currentUserObject.value!!.userId!!)
        docRef.update("likeList", FieldValue.arrayRemove(id))

        updateCurrentUser()
        decrementLiktCount(id)
    }


    fun incrementLikeCount(id: String) {
        firebaseStore.collection("items")
            .document(id)
            .update("likeCount", FieldValue.increment(1))
    }

    fun decrementLiktCount(id: String) {
        firebaseStore.collection("items")
            .document(id)
            .update("likeCount", FieldValue.increment(-1))
    }

    fun incrementLookup(id: String) {
        firebaseStore.collection("items")
            .document(id)
            .update("lookup", FieldValue.increment(1))
    }

    fun clearHomeItem() {
        homeItemList.value = listOf()
    }

    fun clearHomeItemQuery() {
        homeItemQuery = null
    }

    fun getHomeItems() = homeItemList
    fun setHomeItems(categoryList: MutableList<String>) {
        var minGeoPoint =getMinGeoPoint()
        var maxGeoPoint =getMaxGeoPoint()

        if (homeItemQuery == null) { // 처음 불렸을경우
            homeItemQuery = firebaseStore.collection("items")
                .whereIn("category", categoryList)
                .whereGreaterThanOrEqualTo("geoPoint", minGeoPoint)
                .whereLessThanOrEqualTo("geoPoint", maxGeoPoint)
                .orderBy("geoPoint")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(5)
        }

        homeItemQuery!!.get()
            .addOnSuccessListener { result ->
                if (result.isEmpty) return@addOnSuccessListener // 끝까지 다 조회했을

                var tempList = mutableListOf<ItemObject>()
                result.forEach { item ->
                    tempList.add(item.toObject(ItemObject::class.java))
                }

                var beforeList = homeItemList.value!!.toMutableList()
                beforeList.addAll(tempList)
                var afterList = beforeList.toList()

                homeItemList.value = afterList


                // paging을 위해 다음 쿼리 미리 만들어놓는 작업
                homeItemQuery = firebaseStore.collection("items")
                    .whereIn("category", categoryList)
                    .whereGreaterThanOrEqualTo("geoPoint", minGeoPoint)
                    .whereLessThanOrEqualTo("geoPoint", maxGeoPoint)
                    .orderBy("geoPoint")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .startAfter(result.documents[result.size() - 1])
                    .limit(5)
            }
    }

    fun getcategory() = category


    fun setCategory(selectedCategory: String) {
        category.value = selectedCategory
    }

    private fun initCurrentUser() {
        if (firebaseAuth.currentUser == null) return // 앱 삭제하고 재설치

        firebaseStore.collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result!!.exists()) {
                    var document = task.result!!
                    var user = document.toObject<UserObject>(UserObject::class.java)
                    currentUserObject.value = user
                    location.value = currentUserObject.value!!.location
                    loginMode.value = 2
                }
            }
    }

    /*
    FireBase의 currentUserObject 에 변동사항이 생기면 갱신된 유저로 currentUserObject 업데이트 해줌
     */
    private fun updateCurrentUser() {
        firebaseStore.collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result!!.exists()) {
                    var document = task.result!!
                    var user = document.toObject<UserObject>(UserObject::class.java)
                    currentUserObject.value = user
                    location.value = currentUserObject.value!!.location
                }
            }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    // 이미 회원가입 완료 후, back button -> loginActivity -> google login 버튼 클릭시 loginMode =2
                    if (currentUserObject.value?.userId == firebaseAuth.currentUser!!.uid) loginMode.value =
                        2
                    else loginMode.value = 1
                }
            }
    }


    suspend fun firebaseStorageInsertProfile(uri: Uri) {
        var storageRef =
            firebaseStorage.reference.child("userProfileImages").child(firebaseAuth.uid!!)
        storageRef.putFile(uri).continueWithTask { task: Task<UploadTask.TaskSnapshot> ->
            return@continueWithTask storageRef.downloadUrl
        }.addOnSuccessListener { uri ->
            profileUrl = uri.toString()
        }.await()
    }

    suspend fun firebaseStorageInsertItemImage(uri: Uri): String {
        var imageUrl = ""
        var imageFileName = "IMAGE_${SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())}.png"

        var storageRef =
            firebaseStorage.reference.child("itemImages").child(imageFileName)
        storageRef.putFile(uri).continueWithTask { task: Task<UploadTask.TaskSnapshot> ->
            return@continueWithTask storageRef.downloadUrl
        }.addOnSuccessListener { uri ->
            imageUrl = uri.toString()
        }.await()
        return imageUrl
    }

    suspend fun commitUserObject(nickname: String? = "닉네임 없음") {
        val insertUserObject = UserObject(
            firebaseAuth.currentUser!!.uid,
            nickname,
            profileUrl,
            location.value,
            GeoPoint(lat, long)
        )

        firebaseStore.collection("users").document(insertUserObject.userId!!).set(insertUserObject)
            .addOnSuccessListener {
                currentUserObject.value = insertUserObject
                loginMode.value = 2
                isSignSuccess.value = true
            }.await()
    }

    suspend fun commitItemObject(
        imageUrlList: ArrayList<String>,
        title: String,
        overview: String?,
        price: String
    ): String {
        var retRef = ""
        val insertItemObject = ItemObject(
            retRef, // 아래 코드에서, 생성된 retRef로 set 하여 변경
            currentUserObject.value!!.userId,
            currentUserObject.value!!.location,
            imageUrlList,
            title.split(" "),
            category.value,
            overview,
            0,
            price,
            currentUserObject.value!!.geoPoint,
            System.currentTimeMillis()
        )

        var firestoreRef = firebaseStore.collection("items").document()
        firestoreRef.set(insertItemObject)
            .addOnSuccessListener {
                retRef = firestoreRef.id
            }.await()

        firebaseStore.collection("items")
            .document(retRef)
            .set(hashMapOf("id" to retRef), SetOptions.merge())

        return retRef
    }

    fun setLatLong(inputLat: Double, inputLong: Double, application: Application) {
        lat = inputLat
        long = inputLong

        // LatLong to Address
        var mGeocoder = Geocoder(application, Locale.KOREAN)
        var mResultList: List<Address>?
        mResultList = mGeocoder.getFromLocation(lat, long, 1)

        var temp = mResultList[0].getAddressLine(0).split(' ')
        var retVal = temp[2] + " " + temp[3]

        if (!mResultList.isNullOrEmpty()) location.value = retVal
    }

    fun getMinGeoPoint(): GeoPoint {
        var lat = currentUserObject.value!!.geoPoint.latitude
        var long = currentUserObject.value!!.geoPoint.longitude
        return GeoPoint(
            lat - extraArrange,
            long - extraArrange
        )
    }

    fun getMaxGeoPoint(): GeoPoint {
        var lat = currentUserObject.value!!.geoPoint.latitude
        var long = currentUserObject.value!!.geoPoint.longitude
        return GeoPoint(
            lat + extraArrange,
            long + extraArrange
        )
    }



    fun getCurretUser() = currentUserObject
    fun getloginMode() = loginMode
    fun getlocation() = location
    fun getFirebaseAuth() = firebaseAuth
    fun getFirebaseStore() = firebaseStore
    fun getFirebaseStorage() = firebaseStorage
    fun getisSignSuccess() = isSignSuccess
    fun getisWriteSucess() = isWriteSuccess

    fun addItemList(itemRef: String) {
        var docRef = firebaseStore.collection("users").document(currentUserObject.value!!.userId!!)
        docRef.update("itemList", FieldValue.arrayUnion(itemRef))
            .addOnSuccessListener { isWriteSuccess.value = true }
    }


    suspend fun test(): ItemObject {
        lateinit var t: ItemObject
        firebaseStore.collection("items")
            .document("FKOAxjZKCicfz4SU4Slw")
            .get()
            .addOnSuccessListener { result ->
                t = result.toObject(ItemObject::class.java)!!
            }.await()

        return t
    }

}