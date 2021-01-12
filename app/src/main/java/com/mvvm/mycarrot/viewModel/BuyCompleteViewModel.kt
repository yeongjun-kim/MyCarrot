package com.mvvm.mycarrot.viewModel

import android.app.Application
import androidx.lifecycle.*
import com.mvvm.mycarrot.model.ItemObject
import com.mvvm.mycarrot.model.LatestMessageDTO
import com.mvvm.mycarrot.model.UserObject
import com.mvvm.mycarrot.repository.FirebaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class BuyCompleteViewModel(application: Application) : AndroidViewModel(application) {


    /**
     *
     * buyCompleteItem: 거래완료 누른 아이템
     * buyCompleteChatList: buyCompleteItem에 대해 Chat 했던 User List
     */

    private val firebaseRepository: FirebaseRepository
    private var currentUserObject: MutableLiveData<UserObject>

    private var buyCompleteItem: MutableLiveData<ItemObject>
    private var buyCompleteChatList: MutableLiveData<List<LatestMessageDTO>>
    private var selectedBuyer: MutableLiveData<UserObject>

    private var positiveReviewList = MutableLiveData<List<String>>(listOf())
    private var negativeReviewList = MutableLiveData<List<String>>(listOf())

    private var isCommitFinish = MutableLiveData(false)


    init {
        firebaseRepository = FirebaseRepository.getInstance()
        currentUserObject = firebaseRepository.getCurretUser()
        buyCompleteItem = firebaseRepository.getbuyCompleteItem()
        buyCompleteChatList = firebaseRepository.getbuyCompleteChatList()
        selectedBuyer = firebaseRepository.getselectedBuyer()
        positiveReviewList = firebaseRepository.getpositiveReviewList()
        negativeReviewList = firebaseRepository.getnegativeReviewList()
    }

    fun getCurrentUserObject() = currentUserObject


    fun getisCommitFinish() = isCommitFinish
    fun commitReviewToServer(){
        viewModelScope.launch(Dispatchers.IO) {
            isCommitFinish.postValue(false)
            firebaseRepository.commitReviewToServer()
            firebaseRepository.commitItemIdToBuyer()
            isCommitFinish.postValue(true)
        }
    }

    fun getnegativeReviewList() = negativeReviewList
    fun setnegativeReviewList(str:String){
        firebaseRepository.setnegativeReviewList(str)
    }
    fun clearnegativeReviewList(){
        firebaseRepository.clearnegativeReviewList()
    }


    fun getpositiveReviewList() = positiveReviewList
    fun setpositiveReviewList(str:String){
        firebaseRepository.setpositiveReviewList(str)
    }
    fun clearpositiveReviewList(){
        firebaseRepository.clearpositiveReviewList()
    }


    fun getselectedBuyer() = selectedBuyer
    fun setselectedBuyer(userId: String) = firebaseRepository.setselectedBuyer(userId)

    fun getbuyCompleteChatList() = buyCompleteChatList
    fun setbuyCompleteChatList(itemId: String) = firebaseRepository.setbuyCompleteChatList(itemId)


    fun getbuyCompleteItem() = buyCompleteItem
    fun setbuyCompleteItem(itemId: String) = firebaseRepository.setbuyCompleteItem(itemId)


    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return BuyCompleteViewModel(application) as T
        }
    }
}