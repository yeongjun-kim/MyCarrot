package com.mvvm.mycarrot.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import com.mvvm.mycarrot.model.UserObject
import com.mvvm.mycarrot.repository.FirebaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class FirebaseViewModel(application: Application) : AndroidViewModel(application) {

    var firebaseRepository: FirebaseRepository
    private var currentUserObject: MutableLiveData<UserObject>
    private val loginMode: MutableLiveData<Int>
//    private var location: MutableLiveData<String>

    var signupNickname: String? = null
    var signupProfileUrl: String? = null
    var signupLocation: String? = null
    var profileUri: Uri? = null


    init {
        firebaseRepository = FirebaseRepository.getInstance()
        currentUserObject = firebaseRepository.getCurretUser()
        loginMode = firebaseRepository.getloginMode()
//        location = firebaseRepository.getlocation()
    }


    fun getCurrentUser() = currentUserObject

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
//            firebaseRepository.firebaseStorageInsertProfile(profileUri!!)
//            firebaseRepository.commitUserObject(signupNickname)
        }
    }


    fun setLatLong(inputLat: Double, inputLong: Double, application: Application) {
//        firebaseRepository.setLatLong(inputLat, inputLong, application)
    }

    fun setCurrentLatLong(inputLat: Double, inputLong: Double, application: Application) {
//        firebaseRepository.setCurrentLatLong(inputLat, inputLong, application)
    }

    fun getLoginMode() = loginMode

    fun clear() {
        signupNickname = null
        signupProfileUrl = null
        signupLocation = null
    }

    fun getLocation() {
//        location
    }

    fun getisSignSuccess() = firebaseRepository.getisSignSuccess()




    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return FirebaseViewModel(application) as T
        }
    }

}