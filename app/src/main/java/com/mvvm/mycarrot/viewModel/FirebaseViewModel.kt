package com.mvvm.mycarrot.viewModel

import android.app.Application
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.UploadTask
import com.mvvm.mycarrot.model.UserObject
import com.mvvm.mycarrot.repository.FirebaseRepository
import com.mvvm.mycarrot.view.LoginActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FirebaseViewModel(application: Application) : AndroidViewModel(application) {

    var firebaseRepository: FirebaseRepository
    private var curretUserObject: MutableLiveData<UserObject>
    private val loginMode: MutableLiveData<Int>
    private var location: MutableLiveData<String>

    var signupNickname: String? = null
    var signupProfileUrl: String? = null
    var signupLocation: String? = null
    var profileUri: Uri? = null


    init {
        firebaseRepository = FirebaseRepository.getInstance()
        curretUserObject = firebaseRepository.getCurretUser()
        loginMode = firebaseRepository.getloginMode()
        location = firebaseRepository.getlocation()
    }


    fun getCurrentUser() = curretUserObject

    fun firebaseAuthWithGoogle(idToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseRepository.firebaseAuthWithGoogle(idToken)
        }
    }

    fun firebaseStorageInsertProfile() {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseRepository.firebaseStorageInsertProfile(profileUri!!)
        }
    }

    fun commitUserObject() {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseRepository.firebaseStorageInsertProfile(profileUri!!)
            firebaseRepository.commitUserObject(signupNickname)
        }
    }



    fun setLatLong(inputLat: Double, inputLong: Double, application: Application) {
        firebaseRepository.setLatLong(inputLat, inputLong, application)
    }

    fun getLoginMode() = loginMode

    fun clear() {
        signupNickname = null
        signupProfileUrl = null
        signupLocation = null

    }

    fun getLocation() = location

    fun getisSignSuccess() = firebaseRepository.getisSignSuccess()

    fun test(){
        Log.d("fhrm", "FirebaseViewModel -test(),    : ${curretUserObject.value}")
    }


    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return FirebaseViewModel(application) as T
        }
    }

}