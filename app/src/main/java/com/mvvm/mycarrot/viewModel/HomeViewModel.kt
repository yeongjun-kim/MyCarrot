package com.mvvm.mycarrot.viewModel


import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.model.ItemObject
import com.mvvm.mycarrot.model.UserObject
import com.mvvm.mycarrot.repository.FirebaseRepository

class HomeViewModel(application: Application) : AndroidViewModel(application) {

    val firebaseRepository: FirebaseRepository
    private var curretUserObject: MutableLiveData<UserObject>
    private var location: MutableLiveData<String>


    var userId: String? = null
    var userLocation: String? = null
    var progress = MutableLiveData(0)
    var homeItemList: MutableLiveData<List<ItemObject>> = MutableLiveData(listOf())

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

    init {
        firebaseRepository = FirebaseRepository.getInstance()
        curretUserObject = firebaseRepository.getCurretUser()
        userId = curretUserObject.value!!.userId
        userLocation = curretUserObject.value!!.location
        location = firebaseRepository.getlocation()
        homeItemList = firebaseRepository.getHomeItems()
    }

    fun clearHomeItem() {
        firebaseRepository.clearHomeItem()
    }

    fun clearHomeItemQuery() {
        firebaseRepository.clearHomeItemQuery()
    }

    fun getHomeItems() = homeItemList
    fun setHomeItems() {
        firebaseRepository.setHomeItems(categoryList)
    }

    fun clickCustomCheckBox(clickString: String) {
        Log.d("fhrm", "HomeViewModel -clickCustomCheckBox(),    clickString: ${clickString}")
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
    }


    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return HomeViewModel(application) as T
        }
    }


}


