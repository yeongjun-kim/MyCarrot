package com.mvvm.mycarrot.viewModel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import com.mvvm.mycarrot.model.UserObject
import com.mvvm.mycarrot.repository.FirebaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class EditProfileViewModel(application: Application) : AndroidViewModel(application) {


    val firebaseRepository: FirebaseRepository
    private val firebaseStore = FirebaseFirestore.getInstance()
    private var currentUserObject: MutableLiveData<UserObject>
    var newImageUri: Uri? = null
    var newNickname = MutableLiveData<String>()
    var isCommitFinish = MutableLiveData(false)


    init {
        firebaseRepository = FirebaseRepository.getInstance()
        currentUserObject = firebaseRepository.getCurretUser()
        newNickname.value = currentUserObject.value!!.nickname!!
    }

    fun getCurrentUserObject() = currentUserObject

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
            return EditProfileViewModel(application) as T
        }
    }


}