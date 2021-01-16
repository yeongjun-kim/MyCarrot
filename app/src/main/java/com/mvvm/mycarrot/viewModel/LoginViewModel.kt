package com.mvvm.mycarrot.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import com.mvvm.mycarrot.model.UserObject
import com.mvvm.mycarrot.repository.FirebaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    var firebaseRepository: FirebaseRepository
    private val loginMode: MutableLiveData<Int>
    private var isExistAccount: MutableLiveData<Int>
    var nickname = MutableLiveData("")
    var profileImage: Uri? = null


    init {
        firebaseRepository = FirebaseRepository.getInstance()
        loginMode = firebaseRepository.getloginMode()
        isExistAccount = firebaseRepository.getisExistAccount()
    }

    fun getloginMode() = loginMode


    fun getisExistAccount() = isExistAccount
    fun firebaseAuthWithGoogle(idToken: String) {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseRepository.firebaseAuthWithGoogle(idToken)
        }
    }


    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LoginViewModel(application) as T
        }
    }

}