package com.mvvm.mycarrot.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mvvm.mycarrot.model.ItemObject
import com.mvvm.mycarrot.model.UserObject
import com.mvvm.mycarrot.repository.FirebaseRepository

class BuyCompleteViewModel(application:Application) :AndroidViewModel(application){


    /**
     *
     * buyCompleteItem: 거래완료 누른 아이템
     * buyCompleteChatList: buyCompleteItem에 대해 Chat 했던 User List
     */

    private val firebaseRepository: FirebaseRepository
    private var currentUserObject: MutableLiveData<UserObject>
    private var buyCompleteItem = MutableLiveData<ItemObject>()
    private var buyCompleteChatList = MutableLiveData<UserObject>()



    init {
        firebaseRepository = FirebaseRepository.getInstance()
        currentUserObject = firebaseRepository.getCurretUser()
        buyCompleteItem =firebaseRepository.getbuyCompleteItem()
        buyCompleteChatList = firebaseRepository.getbuyCompleteChatList()
    }

    fun getCurrentUserObject() = currentUserObject


    fun getbuyCompleteChatList() =buyCompleteChatList
    fun setbuyCompleteChatList(itemId:String)=firebaseRepository.setbuyCompleteChatList(itemId)


    fun getbuyCompleteItem() = buyCompleteItem
    fun setbuyCompleteItem(itemId:String) =firebaseRepository.setbuyCompleteItem(itemId)



    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return BuyCompleteViewModel(application) as T
        }
    }
}