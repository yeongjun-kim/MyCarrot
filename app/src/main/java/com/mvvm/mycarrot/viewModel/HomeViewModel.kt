package com.mvvm.mycarrot.viewModel


import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.model.ItemObject
import com.mvvm.mycarrot.model.UserObject
import com.mvvm.mycarrot.repository.FirebaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.DisposableHandle
import kotlinx.coroutines.launch

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    /**
     *
     * isFromCategoryFragment: CategoryFm -> replace -> HomeFm 시 Category값이 변경되었나 확인하기 위함
     * tempCategoryList: CategoryFm -> HomeFm 시 카테고리의 변화가 있나 체크하기 위함(HomeFm에서 값 설정 후 CategoryFm로 넘어감)
     *
     */

    val firebaseRepository: FirebaseRepository
    private var currentUserObject: MutableLiveData<UserObject>
    private var location: MutableLiveData<String>
    var selectedItem = MutableLiveData(ItemObject())
    var selectedItemOwner = MutableLiveData(UserObject())
    var isLiked:MutableLiveData<Boolean> = MutableLiveData(false)



    var userId: String? = null
    var userLocation: String? = null
    var progress = MutableLiveData(0)
    var homeItemList: MutableLiveData<List<ItemObject>> = MutableLiveData(listOf())
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
        location = firebaseRepository.getlocation()
        homeItemList = firebaseRepository.getHomeItems()
        selectedItem = firebaseRepository.getselectedItem()
        selectedItemOwner = firebaseRepository.getselectedItemOwner()
        isLiked = firebaseRepository.getIsLiked()
    }

    fun getIsLiked() = isLiked

    fun checkIsLiked() = firebaseRepository.checkIsLiked()

    fun addToLikeList(id: String) = firebaseRepository.addToLikeList(id)

    fun deleteFromLikeList(id: String) = firebaseRepository.deleteFromLikeList(id)

    fun getselectedItem() = selectedItem

    fun getselectedItemOwner() = selectedItemOwner

    fun getCurrentUserObject() = currentUserObject

    fun selectedItem(id: String) = firebaseRepository.selectedItem(id)

    fun selectedItemOwner(id: String) = firebaseRepository.selectedItemOwner(id)

    fun clearHomeItem() = firebaseRepository.clearHomeItem()

    fun clearHomeItemQuery() = firebaseRepository.clearHomeItemQuery()

    fun getHomeItems() = homeItemList

    fun setHomeItems() = firebaseRepository.setHomeItems(categoryList)

    fun clickCustomCheckBox(clickString: String) {
        // TODO("여기서 전체해제 됐을때 최소한 하나는 선택되어야 한다고 하고 그런 작업 들어가야함")
        if (categoryList.contains(clickString)) categoryList.remove(clickString)
        else categoryList.add(clickString)
    }

    fun getLocation() = location
    fun setExtraArrange(progress: Int) {
        if (progress in 0..32) firebaseRepository.extraArrange = 0.01
        else if (progress in 33..65) firebaseRepository.extraArrange = 0.02
        else if (progress in 66..99) firebaseRepository.extraArrange = 0.03
        else firebaseRepository.extraArrange = 0.04
    }

    fun test() {
        Log.d("fhrm", "HomeViewModel -test(),    : click")
    }


    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return HomeViewModel(application) as T
        }
    }


}


