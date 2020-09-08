package com.mvvm.mycarrot.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.auth.GoogleAuthProvider
import com.mvvm.mycarrot.model.UserObject
import com.mvvm.mycarrot.repository.FirebaseRepository
import com.mvvm.mycarrot.view.LoginActivity

class FirebaseViewModel(application: Application) : AndroidViewModel(application) {

    var firebaseRepository: FirebaseRepository
    private var curretUserObject: MutableLiveData<UserObject>
    private val loginMode: MutableLiveData<Int>

    var signupNickname: String? = null
    var signupProfileUrl: String? = null
    var signupLocation: String? = null

    init {
        firebaseRepository = FirebaseRepository.getInstance()
        curretUserObject = firebaseRepository.getCurretUser()
        loginMode = firebaseRepository.getloginMode()
    }


    fun getCurrentUser() = curretUserObject

    fun firebaseAuthWithGoogle(idToken: String) {
        firebaseRepository.firebaseAuthWithGoogle(idToken)
    }

    fun getLoginMode() = loginMode

    fun signupUserObject(){
        firebaseRepository.signupUserObject(signupNickname, signupProfileUrl, signupLocation)
    }

    fun clear() {
        signupNickname = null
        signupProfileUrl = null
        signupLocation = null

    }

    fun setLatLong(inputLat:Double, inputLong:Double, application:Application) {
        firebaseRepository.setLatLong(inputLat, inputLong,application)
    }

    fun getLocation() = firebaseRepository.getlocation()

        class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return FirebaseViewModel(application) as T
        }
    }

}