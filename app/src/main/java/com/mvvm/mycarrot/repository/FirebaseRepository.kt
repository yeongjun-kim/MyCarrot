package com.mvvm.mycarrot.repository

import android.app.Application
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.util.Log
import android.widget.TextView
import androidx.core.net.toUri
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.mvvm.mycarrot.model.ItemObejct
import com.mvvm.mycarrot.model.UserObject
import com.mvvm.mycarrot.view.LoginActivity
import kotlinx.coroutines.tasks.await
import java.text.SimpleDateFormat
import java.util.*

class FirebaseRepository private constructor() {

    /**
     *
     * loginMode: LoginActivity에서 startactvity 할 activity를 정하는 모드
     *  0: startActivity X ( 초기 )
     *  1: startActivity SignUp ( FirebaseAuth 만 가입되어있고, Firestore(users)엔 없는상황 )
     *  2: startActivity MainActivity ( FirebaseAuth , Firestore(users) 에 둘다 존재하는 상황 )
     *
     * curretUserObject: 현재 로그인 한 유저에 대한 UserObject
     * location: 현재 로그인 한 유저의 위치
     * lat: 현재 로그인 한 유저의 latitude
     * long: 현재 로그인 한 유저의 longitude
     * profileUri: 현재 로그인 한 유저의 profile image uri (firestore)
     * category: WriteActivity 에서 선택한 카테고리
     * categoryTextView: CategoryActivity 에서 선택한 카테고리(TextView)
     *
     * isSignSuccess: SigninActivity 에서 observe, 계정등록이 완료되면 true
     */

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseStore = FirebaseFirestore.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()

    var curretUserObject: MutableLiveData<UserObject> = MutableLiveData()
    var loginMode: MutableLiveData<Int> = MutableLiveData(0)
    var location: MutableLiveData<String> = MutableLiveData("관악구 은천동")
    var lat = 37.55
    var long = 126.97
    var profileUrl = ""
    var category:MutableLiveData<String> = MutableLiveData("카테고리 선택")
    var categoryTextView: TextView?=null

    var isSignSuccess:MutableLiveData<Boolean> = MutableLiveData(false)
    var isWriteSuccess:MutableLiveData<Boolean> = MutableLiveData(false)

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

    fun getcategory() = category

    fun getTextView():TextView? {
        Log.d("fhrm", "FirebaseRepository -getTextView(),    categoryTextView==null: ${categoryTextView==null}")
        return categoryTextView
    }

    fun setCategory(selectedCategory: String){
        category.value = selectedCategory
    }

    private fun initCurrentUser() {
        if (firebaseAuth.currentUser == null) return

        firebaseStore.collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result!!.exists()) {
                    var document = task.result!!
                    var user = document.toObject<UserObject>(UserObject::class.java)
                    curretUserObject.value = user
                    loginMode.value = 2
                }
            }
    }

    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    loginMode.value = 1
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

    suspend fun firebaseStorageInsertItemImage(uri: Uri) :String{
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
            location.value
        )

        firebaseStore.collection("users").document(insertUserObject.userId!!).set(insertUserObject)
            .addOnSuccessListener {
                curretUserObject.value = insertUserObject
                loginMode.value = 2
                isSignSuccess.value = true
            }.await()
    }
    suspend fun commitItemObject(imageUrlList:ArrayList<String>, title:String?, overview:String?, price:String):String {
        var retRef = ""
        val insertItemObject = ItemObejct(
            curretUserObject.value!!.userId,
            location.value,
            imageUrlList,
            title,
            category.value,
            overview,
            0,
            price
        )

        var firestoreRef =firebaseStore.collection("items").document()
        firestoreRef.set(insertItemObject)
            .addOnSuccessListener {
                retRef = firestoreRef.id
            }.await()
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

    fun getCurretUser() = curretUserObject
    fun getloginMode() = loginMode
    fun getlocation() = location
    fun getFirebaseAuth() = firebaseAuth
    fun getFirebaseStore() = firebaseStore
    fun getFirebaseStorage() = firebaseStorage
    fun getisSignSuccess() = isSignSuccess
    fun getisWriteSucess() = isWriteSuccess

    fun addItemList(itemRef: String) {
        var  docRef=firebaseStore.collection("users").document(curretUserObject.value!!.userId!!)
        docRef.update("itemList", FieldValue.arrayUnion(itemRef)).addOnSuccessListener { isWriteSuccess.value = true }
    }

    suspend fun test() {
        var a =firebaseStore.collection("test").document("test")
        a.update("array", FieldValue.arrayRemove("one")).await()
    }

}

