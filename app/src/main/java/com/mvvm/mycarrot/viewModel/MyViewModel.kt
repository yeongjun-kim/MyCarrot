package com.mvvm.mycarrot.viewModel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.firestore.FirebaseFirestore
import com.mvvm.mycarrot.model.ItemObject
import com.mvvm.mycarrot.model.UserObject
import com.mvvm.mycarrot.repository.FirebaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class MyViewModel(application: Application) : AndroidViewModel(application) {

    val firebaseRepository: FirebaseRepository
    private var currentUserObject: MutableLiveData<UserObject>
    var isStartItemActivity: MutableLiveData<Int> = MutableLiveData(0)

    var newImageUri: Uri? = null //editprofile
    var newNickname = MutableLiveData<String>()//editprofile
    var isCommitFinish = MutableLiveData(false)//editprofile

    var likeItemList: MutableLiveData<List<ItemObject>> = MutableLiveData(listOf()) // likelist
    var collectItemList: MutableLiveData<List<ItemObject>> = MutableLiveData(listOf())
    var buyItemList: MutableLiveData<List<ItemObject>> = MutableLiveData(listOf())
    var myItemList: MutableLiveData<List<ItemObject>> = MutableLiveData(listOf())


    init {
        firebaseRepository = FirebaseRepository.getInstance()
        currentUserObject = firebaseRepository.getCurretUser()
        isStartItemActivity = firebaseRepository.getIsStartItemActivity()
        likeItemList = firebaseRepository.getlikeItemList()
        collectItemList = firebaseRepository.getcollectItemList()
        buyItemList = firebaseRepository.getbuyItemList()
        myItemList = firebaseRepository.getmyItemList()
        newNickname.value = currentUserObject.value!!.nickname!!
    }

    fun changeItemStatus(itemId: String, status: String) {
        firebaseRepository.changeItemStatus(itemId, status)
    }


    fun getbuyItemList() = buyItemList
    fun setbuyItemList() {
        firebaseRepository.setbuyItemList()
    }


    fun getmyItemList() = myItemList
    fun setmyItemList() {
        firebaseRepository.setmyItemList()
    }

    fun getcollectItemList() = collectItemList
    fun setcollectItemList() {
        firebaseRepository.setcollectItemList()
    }

    fun getlikeItemList() = likeItemList
    fun setlikeItemList() {
        firebaseRepository.setlikeItemList()
    }

    fun getIsStartItemActivity() = firebaseRepository.getIsStartItemActivity()

    fun getselectedFragment() = firebaseRepository.getselectedFragment()
    fun setselectedItem(id: String, fm: String) = firebaseRepository.setSelectedItem(id, fm)
    fun setselectedItemOwner(id: String, fm: String) =
        firebaseRepository.setSelectedItemOwner(id, fm)

    fun clearIsStartItemActivity() = firebaseRepository.clearIsStartItemActivity()


    fun getCurrentUserObject() = currentUserObject

    //editprofile
    fun commitChangeInfo() {

        if (newImageUri == null) {
            viewModelScope.launch(Dispatchers.IO) {
                firebaseRepository.updateFirestore(
                    "users",
                    firebaseRepository.getFirebaseAuth().uid!!,
                    "nickname",
                    newNickname.value!!
                )
                    .await()

                firebaseRepository.updateCurrentUser().await()
                isCommitFinish.postValue(true)
            }
        } else {
            viewModelScope.launch(Dispatchers.IO) {

                firebaseRepository.firebaseStorageInsertProfile(newImageUri!!)
                firebaseRepository.updateFirestore(
                    "users",
                    firebaseRepository.getFirebaseAuth().uid!!,
                    "nickname",
                    newNickname.value!!,
                    true
                )
                    .await()

                firebaseRepository.updateCurrentUser().await()
                isCommitFinish.postValue(true)

            }
        }
    }


    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return MyViewModel(application) as T
        }
    }
}