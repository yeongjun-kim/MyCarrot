package com.mvvm.mycarrot.data.repository

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ktx.database
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.mvvm.mycarrot.data.model.ItemObject
import com.mvvm.mycarrot.data.model.LatestMessageDTO
import com.mvvm.mycarrot.data.model.UserObject
import com.mvvm.mycarrot.data.db.Location
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
     * currentUserObject: 현재 로그인 한 유저에 대한 UserObject
     * profileUri: 현재 로그인 한 유저의 profile image uri (firestore)
     * category: WriteActivity 에서 선택한 카테고리
     * extraArrange: 현재 lat, lang 기준 플러스마이너스 위경도 범위
     *
     * isExistAccount: LoginFragment 에서 Observe, 이미 Firestore에 등록한 정보가 있다면2, 아니면 1
     * isSignSuccess: SigninActivity 에서 observe, 계정등록이 완료되면 true
     * isStartItemActivity: HomeFragment 에서 ItemActivity 로 넘어가기 전, selectedItem/selectedItemOwner 값을 모두 세팅 해야 2
     * isCertificationFinish: NeighborhoodCertificationActivity 에서 동네인증 완료하기 버튼 클릭시 Firestore의 locationCertification 값 증가 후 완료를 알림
     *
     * homeItemList: HomeFragent 에 보여질 item List
     * homeItemQuery: homeItemList 를 firestore 에서 get 할때 paging에 쓰기위함 (null = 첫페이지, not null = 첫페이지X)
     * collectItemList: CoolectActivity 에 보여질 item List
     *
     * selectedItem: HomeFragment 에서 클릭 한 Item (ItemActivity)
     * selectedItemOwner: HomeFragment 에서 클릭 한 Item Owner (ItemActivity)
     * selectedItemOwnersItem: ItemActivity 에서 [더보기] 클릭시 SeemoreViewModel 에서 가져가 SeeMoreActivity 에서 보여줄 List
     * selectedFragment: 어느 Activity/Fragment 에서 아이템 클릭하였는지 (homeFm, itemAv, searchFm 등등.. / observe할때 isActvity==2 에서 겹쳐가지고)
     *
     * buyItemList: BuyItemActivity 에서 쓰일 내가 구매한 아이템내역
     * myItemList: SellListActivity 에서 쓰일 나의 판매내역
     *
     * buyCompleteItem: 구매완료 누르는 아이템 세팅 (BuyCompleteActivity)
     * buyCompleteChatList: buyCompleteItem에 대해 Chat 나눴던 User List (BuyCompleteActivity)
     * selectedBuyer: buyCompleteChatList에서 선택된 User (SendReviewFragment)
     * positiveReviewList: CheckBox를 통해 긍정평가 클릭하면 해당 내용 저장 (SendReviewFragment)
     * negativeReviewList: CheckBox를 통해 부평가 클릭하면 해당 내용 저장 (SendReviewFragment)
     */

    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseStore = FirebaseFirestore.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance()


    private var currentUserObject: MutableLiveData<UserObject> = MutableLiveData()
    private var loginMode: MutableLiveData<Int> = MutableLiveData(0)
    private var currentLat = 37.55
    private var currentLong = 126.97

    private var profileUrl = ""
    private var category: MutableLiveData<String> = MutableLiveData("카테고리 선택")
    private var extraArrange: Double = 0.01

    private var isExistAccount = MutableLiveData(0)
    private var isSignSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    private var isWriteSuccess: MutableLiveData<Boolean> = MutableLiveData(false)
    private var isStartItemActivity: MutableLiveData<Int> = MutableLiveData(0)
    private var isCertificationFinish: MutableLiveData<Boolean> = MutableLiveData(false)

    private var homeItemQuery: Query? = null
    private var homeItemList: MutableLiveData<List<ItemObject>> = MutableLiveData(listOf())
    private var collectItemList: MutableLiveData<List<ItemObject>> = MutableLiveData(listOf())
    private var likeItemList: MutableLiveData<List<ItemObject>> = MutableLiveData(listOf())

    private var selectedItem = ItemObject()
    private var selectedItemOwner = UserObject()
    private var selectedItemOwnersItem: List<ItemObject> = listOf()
    private var selectedFragment = ""

    private var buyItemList: MutableLiveData<List<ItemObject>> = MutableLiveData(listOf())
    private var myItemList: MutableLiveData<List<ItemObject>> = MutableLiveData(listOf())

    private var buyCompleteItem = MutableLiveData<ItemObject>()
    private var buyCompleteChatList = MutableLiveData<List<LatestMessageDTO>>()
    private var selectedBuyer = MutableLiveData<UserObject>()
    private val positiveReviewList = MutableLiveData<List<String>>(listOf())
    private val negativeReviewList = MutableLiveData<List<String>>(listOf())

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
    }

    fun getselectedItemOwnersItem() = selectedItemOwnersItem
    fun setselectedItemOwnersItem(list:List<ItemObject>) {
        selectedItemOwnersItem = list
    }


    private fun initCurrentUser() {
        // 앱 삭제하고 재설치
        if (firebaseAuth.currentUser == null) {
            loginMode.postValue(1)
            return
        }

        firebaseStore.collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result!!.exists()) {
                    var document = task.result!!
                    var user = document.toObject<UserObject>(UserObject::class.java)
                    currentUserObject.value = user
                    loginMode.value = 2
                    initFCMtoken()
                } else {
                    loginMode.postValue(1)
                }
            }
    }

    fun getisExistAccount() = isExistAccount
    fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        firebaseAuth.signInWithCredential(credential)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    firebaseStore.collection("users")
                        .document(firebaseAuth.currentUser!!.uid)
                        .get()
                        .addOnCompleteListener { task ->
                            // 이미 회원가입을 했던 유저라면
                            if (task.isSuccessful && task.result!!.exists()) {
                                var document = task.result!!
                                var user = document.toObject<UserObject>(UserObject::class.java)
                                currentUserObject.value = user
                                isExistAccount.postValue(2)
                                initFCMtoken()
                            } else {
                                isExistAccount.postValue(1)
                            }
                        }

                }
            }
    }

    suspend fun firebaseStorageInsertProfile(uri: Uri) {
        var storageRef =
            firebaseStorage.reference.child("userProfileImages").child(firebaseAuth.uid!!)
        storageRef.putFile(uri).continueWithTask {
            return@continueWithTask storageRef.downloadUrl
        }.addOnSuccessListener { uri ->
            profileUrl = uri.toString()
        }.await()
    }

    suspend fun commitUserObject(nickname: String, location: Location) {
        val insertUserObject = UserObject(
            firebaseAuth.currentUser!!.uid,
            nickname,
            profileUrl,
            36.5,
            "${location.str.split(" ")[1]} ${location.str.split(" ")[2]}",
            0,
            System.currentTimeMillis(),
            System.currentTimeMillis(),
            GeoPoint(location.latitude!!, location.longitude!!)
        )

        firebaseStore.collection("users").document(insertUserObject.userId!!).set(insertUserObject)
            .addOnSuccessListener {
                currentUserObject.value = insertUserObject
                loginMode.value = 2
                initFCMtoken()
                isSignSuccess.value = true
            }.await()
    }

    /*
    구매 확정된 아이템 ID를 구매자의 buyList에 추가
     */
    suspend fun commitItemIdToBuyer() {
        var docRef = firebaseStore.collection("users").document(selectedBuyer.value!!.userId!!)
        docRef.update("buyList", FieldValue.arrayUnion(buyCompleteItem.value!!.id)).await()
    }

    /*
    선택한 리뷰를 해당 유저에 commit
     */
    suspend fun commitReviewToServer() {
        var docRef = firebaseStore.collection("users").document(selectedBuyer.value!!.userId!!)

        positiveReviewList.value?.forEach {
            if (it == "제가 있는 곳까지 와서 거래했어요.") docRef.update(
                "positive_1",
                FieldValue.increment(1)
            ).await()
            else if (it == "친절하고 매너가 좋아요.") docRef.update(
                "positive_2",
                FieldValue.increment(1)
            ).await()
            else if (it == "시간 약속을 잘 지켜요.") docRef.update(
                "positive_3",
                FieldValue.increment(1)
            ).await()
            else if (it == "응답이 빨라요.") docRef.update("positive_4", FieldValue.increment(1)).await()
        }

        negativeReviewList.value?.forEach {
            if (it == "무리하게 가격을 깎아요.") docRef.update("negative_1", FieldValue.increment(1)).await()
            else if (it == "시간약속을 안 지켜요.") docRef.update(
                "negative_2",
                FieldValue.increment(1)
            ).await()
            else if (it == "무조건 택배거래만 하려고 해요.") docRef.update(
                "negative_3",
                FieldValue.increment(1)
            ).await()
            else if (it == "채팅 메시지를 보내도 답이 없어요.") docRef.update(
                "negative_4",
                FieldValue.increment(1)
            ).await()
        }

        // 리뷰작업 후 온도 변경
        docRef
            .get()
            .addOnSuccessListener {
                var user = it.toObject(UserObject::class.java)!!
                val positive = user.positive_1 + user.positive_2 + user.positive_3 + user.positive_4
                val negative = user.negative_1 + user.negative_2 + user.negative_3 + user.negative_4

                var newTemperature = 36.5 + String.format(
                    "%.1f",
                    (positive.toDouble() - negative.toDouble()) / 50
                ).toDouble()
                docRef.update("temperature", newTemperature)
            }
            .await()

    }

    fun addToItemChatList(myId: String, itemId: String) {
        firebaseStore.collection("items")
            .document(itemId)
            .update("chatList", FieldValue.arrayUnion(myId))
    }

    fun getnegativeReviewList() = negativeReviewList
    fun setnegativeReviewList(str: String) {
        var tempList = negativeReviewList.value!!.toMutableList()

        if (negativeReviewList.value!!.contains(str)) {
            tempList.remove(str)
            negativeReviewList.value = tempList.toList()
        } else {
            tempList.add(str)
            negativeReviewList.value = tempList.toList()
        }
    }

    fun clearnegativeReviewList() {
        negativeReviewList.value = listOf()
    }


    fun getpositiveReviewList() = positiveReviewList
    fun setpositiveReviewList(str: String) {
        var tempList = positiveReviewList.value!!.toMutableList()

        if (positiveReviewList.value!!.contains(str)) {
            tempList.remove(str)
            positiveReviewList.value = tempList.toList()
        } else {
            tempList.add(str)
            positiveReviewList.value = tempList.toList()
        }
    }

    fun clearpositiveReviewList() {
        positiveReviewList.value = listOf()
    }

    /*
    구매자 목록(buyCompleteChatList) 에서 선택된 userId를 통해 UserObject 가져오기.
     */
    fun getselectedBuyer() = selectedBuyer

    fun setselectedBuyer(userId: String) {
        firebaseStore.collection("users").document(userId)
            .get()
            .addOnSuccessListener { result ->
                selectedBuyer.value = result.toObject(UserObject::class.java)
            }

    }

    /*
    구매자 목록은 latest-messages 에서 해당 Item에 대해 대화를 나눈 유저로 한정.
     */
    fun getbuyCompleteChatList() = buyCompleteChatList

    fun setbuyCompleteChatList(itemId: String) {
        Firebase.database.reference.child("/latest-messages/${currentUserObject.value!!.userId}")
            .addChildEventListener(object : ChildEventListener {
                override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {

                    var list = mutableListOf<LatestMessageDTO>()

                    snapshot.children.forEach { result ->
                        if (result.key == itemId) {
                            val message = result.getValue(LatestMessageDTO::class.java) ?: return
                            list.add(message)
                        }
                    }

                    buyCompleteChatList.value = list.toList()

                }

                override fun onCancelled(error: DatabaseError) {}
                override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}
                override fun onChildRemoved(snapshot: DataSnapshot) {}
            })
    }

    fun getbuyCompleteItem() = buyCompleteItem
    fun setbuyCompleteItem(itemId: String) {
        firebaseStore.collection("items").document(itemId)
            .get()
            .addOnSuccessListener { result ->
                buyCompleteItem.value = result.toObject(ItemObject::class.java)
            }
    }


    fun changeItemStatus(itemId: String, status: String) {
        var idx = myItemList.value!!.indexOfFirst { it.id == itemId }
        myItemList.value!![idx].status = status
        myItemList.value = myItemList.value

        var ref = firebaseStore.collection("items").document(itemId)
        ref.update("status", status)

    }


    fun clearBuyItemList() {
        buyItemList.value = listOf()
    }

    fun getbuyItemList() = buyItemList
    fun setbuyItemList() {
        clearBuyItemList()
        currentUserObject.value!!.buyList.forEach { itmeId ->
            firebaseStore.collection("items")
                .document(itmeId)
                .get()
                .addOnSuccessListener { result ->
                    var item = result.toObject(ItemObject::class.java)!!
                    var tempList = buyItemList.value!!.toMutableList()
                    tempList.add(item)
                    buyItemList.postValue(tempList)
                }
        }
    }

    fun clearMyItemList() {
        myItemList.value = listOf()
    }

    fun getmyItemList() = myItemList
    fun setmyItemList() {
        clearMyItemList()
        currentUserObject.value!!.itemList.forEach { itmeId ->
            firebaseStore.collection("items")
                .document(itmeId)
                .get()
                .addOnSuccessListener { result ->
                    var item = result.toObject(ItemObject::class.java)!!
                    var tempList = myItemList.value!!.toMutableList()
                    tempList.add(item)
                    myItemList.postValue(tempList)
                }
        }
    }


    fun getIsCertificationFinish() = isCertificationFinish
    fun clearIsCertificationFinish() {
        isCertificationFinish.postValue(false)
    }

    fun doCertification() {
        var docRef = firebaseStore.collection("users").document(currentUserObject.value!!.userId!!)
        docRef.update("locationCertification", currentUserObject.value!!.locationCertification + 1)
            .addOnSuccessListener {
                isCertificationFinish.postValue(true)
            }
    }

    fun getCurrentLatLong() = Pair(currentLat, currentLong)
    fun setCurrentLatLong(lat: Double, long: Double) {
        currentLat = lat
        currentLong = long
    }


    /*
    로그인 성공시 마지막 로그인 시간 Update
     */
    fun refreshLastLoginTime() {
        var docRef = firebaseStore.collection("users").document(currentUserObject.value!!.userId!!)
        docRef.update("lastLoginTime", System.currentTimeMillis())

        updateCurrentUser()
    }

    fun getIsStartItemActivity() = isStartItemActivity
    fun clearIsStartItemActivity() {
        isStartItemActivity.value = 0
    }

    fun getselectedFragment() = selectedFragment
    fun setelectedFragment(fragment: String) {
        selectedFragment = fragment
    }

    fun getselectedItem() = selectedItem
    fun setSelectedItem(id: String, fm: String) {
        incrementLookup(id)

        setelectedFragment(fm)
        firebaseStore.collection("items")
            .document(id)
            .get()
            .addOnSuccessListener { result ->
                selectedItem = result.toObject(ItemObject::class.java)!!
                isStartItemActivity.value = isStartItemActivity.value!!.plus(1)
            }
    }

    fun getselectedItemOwner() = selectedItemOwner
    fun setSelectedItemOwner(id: String, fm: String) {

        setelectedFragment(fm)
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

    fun addToLikeUserList(id: String) {
        var docRef = firebaseStore.collection("users").document(currentUserObject.value!!.userId!!)
        docRef.update("likeUserList", FieldValue.arrayUnion(id))

        updateCurrentUser()
    }

    fun deleteFromLikeUserList(id: String) {
        var docRef = firebaseStore.collection("users").document(currentUserObject.value!!.userId!!)
        docRef.update("likeUserList", FieldValue.arrayRemove(id))

        updateCurrentUser()
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

    fun clearCollectItemList() {
        collectItemList.value = listOf()
    }

    fun clearlikeItemList() {
        likeItemList.value = listOf()
    }

    fun getlikeItemList() = likeItemList
    fun setlikeItemList() {
        clearlikeItemList()

        currentUserObject.value!!.likeList.forEach { itemId ->

            FirebaseFirestore.getInstance().collection("items").document(itemId)
                .get()
                .addOnSuccessListener { result ->
                    var item = result.toObject(ItemObject::class.java)!!
                    var temp = likeItemList.value!!.toMutableList()
                    temp.add(item)
                    likeItemList.value = temp.toList()
                }
        }
    }

    fun getcollectItemList() = collectItemList
    fun setcollectItemList() {
        clearCollectItemList()

        currentUserObject.value!!.likeUserList.forEach { targetUid ->

            FirebaseFirestore.getInstance().collection("users").document(targetUid)
                .get()
                .addOnSuccessListener { result ->
                    var user = result.toObject(UserObject::class.java)!!
                    user.itemList.forEach { itemId ->
                        FirebaseFirestore.getInstance().collection("items").document(itemId)
                            .get()
                            .addOnSuccessListener {
                                var item = it.toObject(ItemObject::class.java)!!
                                var temp = collectItemList.value!!.toMutableList()
                                temp.add(item)
                                collectItemList.value = temp.toList()
                            }
                    }
                }
        }
    }


    fun clearHomeItem() {
        homeItemList.value = listOf()
    }

    fun clearHomeItemQuery() {
        homeItemQuery = null
    }

    fun getHomeItems() = homeItemList
    fun setHomeItems(categoryList: MutableList<String>) {
        var minGeoPoint = getMinGeoPoint()
        var maxGeoPoint = getMaxGeoPoint()

        if (homeItemQuery == null) { // 처음 불렸을경우
            homeItemQuery = firebaseStore.collection("items")
                .whereIn("category", categoryList)
                .whereGreaterThanOrEqualTo("geoPoint", minGeoPoint)
                .whereLessThanOrEqualTo("geoPoint", maxGeoPoint)
                .orderBy("geoPoint")
                .orderBy("timestamp", Query.Direction.DESCENDING)
                .limit(10)
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
                    .limit(10)
            }
    }

    fun getcategory() = category


    fun setCategory(selectedCategory: String) {
        category.value = selectedCategory
    }

    private fun initFCMtoken() {

        FirebaseMessaging.getInstance().token.addOnSuccessListener { token ->
            Firebase.database.reference.child("/user-token/${currentUserObject.value!!.userId}")
                .setValue(token)
        }
    }


    /*
    Firestore의 field update.
    isUriAlsoChange = true 일 시, EditProfile에서 Profile Image 또한 변경이 생겼을때.
     */
    fun updateFirestore(
        collection: String,
        document: String,
        field: String,
        value: String,
        isUriAlsoChange: Boolean = false
    ) =
        if (isUriAlsoChange) {
            firebaseStore.collection(collection)
                .document(document)
                .update(
                    mapOf(
                        field to value,
                        "profileUrl" to profileUrl
                    )
                )
                .addOnSuccessListener { }
        } else {
            firebaseStore.collection(collection)
                .document(document)
                .update(field, value)
                .addOnSuccessListener { }
        }

    /*
    FireBase의 currentUserObject 에 변동사항이 생기면 갱신된 유저로 currentUserObject 업데이트 해줌
     */
    fun updateCurrentUser() =
        firebaseStore.collection("users")
            .document(firebaseAuth.currentUser!!.uid)
            .get()
            .addOnCompleteListener { task ->
                if (task.isSuccessful && task.result!!.exists()) {
                    var document = task.result!!
                    var user = document.toObject<UserObject>(UserObject::class.java)
                    currentUserObject.value = user
                }
            }


    suspend fun firebaseStorageInsertItemImage(uri: Uri): String {
        var imageUrl = ""
        var imageFileName = "IMAGE_${SimpleDateFormat("yyyyMMdd_HHmmss").format(Date())}.png"

        var storageRef =
            firebaseStorage.reference.child("itemImages").child(imageFileName)
        storageRef.putFile(uri).continueWithTask {
            return@continueWithTask storageRef.downloadUrl
        }.addOnSuccessListener { uri ->
            imageUrl = uri.toString()
        }.await()
        return imageUrl
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
            currentUserObject.value!!.nickname,
            currentUserObject.value!!.location,
            "sell",
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

    fun setextraArrange(d: Double) {
        extraArrange = d
    }

    fun getextraArrange() = extraArrange
    fun getCurretUser() = currentUserObject
    fun getloginMode() = loginMode
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