package com.mvvm.mycarrot.viewModel

import android.app.Application
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.net.Uri
import android.util.Log
import androidx.lifecycle.*
import com.bumptech.glide.Glide.init
import com.mvvm.mycarrot.model.UserObject
import com.mvvm.mycarrot.repository.FirebaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WriteViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseRepository: FirebaseRepository
    private var curretUserObject: MutableLiveData<UserObject>

    var userId: String? = null
    var userLocation: String? = null
    var imageList: ArrayList<String> = arrayListOf()
    var uriImageList: ArrayList<Uri> = arrayListOf()
    var title: String? = null
    var categoryList: ArrayList<String> = arrayListOf()
    var overview: String? = null
    var lookup: Long = 0
    var price: String = ""

    init {
        firebaseRepository = FirebaseRepository.getInstance()
        curretUserObject = firebaseRepository.getCurretUser()
        userId = curretUserObject.value!!.userId
        userLocation = curretUserObject.value!!.location
    }

    fun getisWirteSuccess() = firebaseRepository.getisWriteSucess()

    fun addUriToList(uri: Uri) {
        if (uriImageList.size > 10) return

        uriImageList.add(uri)
    }

    fun commitItemObject() {
        viewModelScope.launch(Dispatchers.IO) {
            uriImageList.forEachIndexed { index, uri ->
                imageList.add(firebaseRepository.firebaseStorageInsertItemImage(uri))
            }
            var itemRef = firebaseRepository.commitItemObject(imageList, title, categoryList, overview, price)
            // itemRef는 방금 넣은 아이템의 firestore id니까 이거를 user콜렉션의 itemlist에 넣을것
            firebaseRepository.addItemList(itemRef)
        }
    }

    fun test() {
        viewModelScope.launch(Dispatchers.IO) {
            firebaseRepository.test()
        }
    }

    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return WriteViewModel(application) as T
        }
    }


}