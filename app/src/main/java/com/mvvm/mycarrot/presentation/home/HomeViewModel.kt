package com.mvvm.mycarrot.presentation.home


import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mvvm.mycarrot.data.model.ItemObject
import com.mvvm.mycarrot.data.model.UserObject
import com.mvvm.mycarrot.data.repository.FirebaseRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    /**
     *
     * isFromCategoryFragment: CategoryFm -> replace -> HomeFm 시 Category값이 변경되었나 확인하기 위함
     * tempCategoryList: CategoryFm -> HomeFm 시 카테고리의 변화가 있나 체크하기 위함(HomeFm에서 값 설정 후 CategoryFm로 넘어감)
     * selectedItemOwnersItem: HomeFragment 에서 클릭 한 Item Owner 의 다른 상품들 목록 (ItemActivity)
     * selectedItemRecommendItem: HomeFragment 에서 클릭 한 Item 과 같은 카테고리에 있는 추천 상품들 목록(ItemActivity)
     *
     */

    val firebaseRepository: FirebaseRepository
    private val firebaseStore = FirebaseFirestore.getInstance()
    private var currentUserObject: MutableLiveData<UserObject>

    var isStartItemActivity: MutableLiveData<Int> = MutableLiveData(0)

    var userId: String? = null
    var userLocation: String? = null
    var progress = MutableLiveData(0)
    var homeItemList: MutableLiveData<List<ItemObject>> = MutableLiveData(listOf())

    var selectedItem = ItemObject()
    var selectedItemOwner = UserObject()
    var selectedItemOwnersItem: MutableLiveData<List<ItemObject>> = MutableLiveData(listOf())
    var selectedItemRecommendItem: MutableLiveData<List<ItemObject>> = MutableLiveData(listOf())


    var isFromCategoryFragment = false
    var categoryList = mutableListOf(
        "디지털/가전",
        "가구/인테리어",
        "생활/가공식품",
        "여성패션/잡화",
        "남성패션/잡화",
        "게임/취미/스포츠/레저",
        "뷰티/미용",
        "유아/반려동물용품",
        "도서/티켓/음반",
        "삽니다"
    )
    var tempCategoryList = categoryList.toMutableList()

    init {
        firebaseRepository = FirebaseRepository.getInstance()
        currentUserObject = firebaseRepository.getCurretUser()
        userId = currentUserObject.value!!.userId
        userLocation = currentUserObject.value!!.location
        homeItemList = firebaseRepository.getHomeItems()
        selectedItem = firebaseRepository.getselectedItem()
        selectedItemOwner = firebaseRepository.getselectedItemOwner()
        selectedItemOwnersItem.value = firebaseRepository.getselectedItemOwnersItem()
        isStartItemActivity = firebaseRepository.getIsStartItemActivity()
        initDefaultProgress()
    }

    private fun initDefaultProgress() {
        if (firebaseRepository.getextraArrange() == 0.01) progress.value = 0
        else if (firebaseRepository.getextraArrange() == 0.02) progress.value = 33
        else if (firebaseRepository.getextraArrange() == 0.03) progress.value = 66
        else progress.value = 100
    }


    fun refreshLastLoginTime() = firebaseRepository.refreshLastLoginTime()
    fun clearIsStartItemActivity() = firebaseRepository.clearIsStartItemActivity()
    fun getIsStartItemActivity() = firebaseRepository.getIsStartItemActivity()
    fun getselectedFragment() = firebaseRepository.getselectedFragment()
    fun getselectedItem() = selectedItem
    fun setselectedItem(id: String, fm: String) = firebaseRepository.setSelectedItem(id, fm)
    fun getselectedItemOwner() = selectedItemOwner
    fun setselectedItemOwner(id: String, fm: String) =
        firebaseRepository.setSelectedItemOwner(id, fm)

    fun addToLikeList(id: String) = firebaseRepository.addToLikeList(id)
    fun deleteFromLikeList(id: String) = firebaseRepository.deleteFromLikeList(id)

    fun addToLikeUserList(id: String) = firebaseRepository.addToLikeUserList(id)
    fun deleteFromLikeUserList(id: String) = firebaseRepository.deleteFromLikeUserList(id)


    fun getCurrentUserObject() = currentUserObject
    fun clearHomeItem() = firebaseRepository.clearHomeItem()
    fun clearHomeItemQuery() = firebaseRepository.clearHomeItemQuery()
    fun getHomeItems() = homeItemList
    fun setHomeItems() = firebaseRepository.setHomeItems(categoryList)
    fun getCurrentLatLong() = firebaseRepository.getCurrentLatLong()
    fun getIsCertificationFinish() = firebaseRepository.getIsCertificationFinish()
    fun clearIsCertificationFinish() = firebaseRepository.clearIsCertificationFinish()
    fun doCertification() = firebaseRepository.doCertification()



    fun addToItemChatList(myId: String, itemId: String) {
        firebaseRepository.addToItemChatList(myId, itemId)
    }

    /*
    아이템 Click 하여 selectedItem 작업이 끝나면, 불리는 함수로
    해당 Item과 같은 카테고리에 있는 다른 아이템 목록들을 selectedItemRecommendItem 에 저장한다.
    .*/
    fun getselectedItemRecommendItem() = selectedItemRecommendItem

    fun setSelectedItemRecommendItem(itemObject: ItemObject = selectedItem) {
        var minGeoPoint = firebaseRepository.getMinGeoPoint()
        var maxGeoPoint = firebaseRepository.getMaxGeoPoint()

        firebaseStore.collection("items")
            .whereEqualTo("id", itemObject.id)
            .get()
            .addOnSuccessListener { snapshot ->
                firebaseStore.collection("items")
                    .whereEqualTo("category", itemObject.category)
                    .whereGreaterThanOrEqualTo("geoPoint", minGeoPoint)
                    .whereLessThanOrEqualTo("geoPoint", maxGeoPoint)
                    .orderBy("geoPoint")
                    .orderBy("timestamp", Query.Direction.DESCENDING)
                    .startAfter(snapshot.documents[0])
                    .limit(10)
                    .get()
                    .addOnSuccessListener { result ->
                        if (result.isEmpty) return@addOnSuccessListener

                        selectedItemRecommendItem.value =
                            result.map { it.toObject(ItemObject::class.java) }
                    }
            }
    }


    /*
    아이템 Click 하여 selectedItemOwner 작업이 끝나면, 불리는 함수로
    해당 Owner의 다른 아이템 목록들을 selectedItemOwnersItem 에 저장한다.
     */
    fun getselectedItemOwnersItem() = selectedItemOwnersItem

    fun setSelectedItemOwnersItem(itemList: ArrayList<String> = selectedItemOwner.itemList) {

        selectedItemOwnersItem.value = listOf()


        itemList.forEach {
            firebaseStore.collection("items")
                .document(it)
                .get()
                .addOnSuccessListener { result ->
                    var item = result.toObject(ItemObject::class.java)

                    var currentList = selectedItemOwnersItem.value
                    var conversionList = currentList!!.toMutableList()

                    conversionList.add(item!!)
                    selectedItemOwnersItem.value = conversionList.toList()
                }
        }

    }


    fun saveSelectedItemOwnersItem() {
        firebaseRepository.setselectedItemOwnersItem(selectedItemOwnersItem.value!!)
    }


    fun clickCustomCheckBox(clickString: String) {
        // TODO("여기서 전체해제 됐을때 최소한 하나는 선택되어야 한다고 하고 그런 작업 들어가야함")
        if (categoryList.contains(clickString)) categoryList.remove(clickString)
        else categoryList.add(clickString)
    }

    fun getExtraArrange() = firebaseRepository.getextraArrange()
    fun setExtraArrange(progress: Int) {
        if (progress in 0..32) firebaseRepository.setextraArrange(0.01)
        else if (progress in 33..65) firebaseRepository.setextraArrange(0.02)
        else if (progress in 66..99) firebaseRepository.setextraArrange(0.03)
        else firebaseRepository.setextraArrange(0.04)


    }

    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return HomeViewModel(application) as T
        }
    }


}