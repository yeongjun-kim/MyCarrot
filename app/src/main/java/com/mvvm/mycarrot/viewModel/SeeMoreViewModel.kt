package com.mvvm.mycarrot.viewModel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mvvm.mycarrot.model.ItemObject
import com.mvvm.mycarrot.repository.FirebaseRepository

class SeeMoreViewModel(application: Application) : AndroidViewModel(application) {
    val firebaseRepository: FirebaseRepository
    var itemList: List<ItemObject>

    init {
        firebaseRepository = FirebaseRepository.getInstance()
        itemList = firebaseRepository.selectedItemOwnersItem
    }


    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return SeeMoreViewModel(application) as T
        }
    }
}