package com.mvvm.mycarrot.viewModel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import com.mvvm.mycarrot.model.UserObject
import com.mvvm.mycarrot.repository.FirebaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WriteViewModel(application: Application) : AndroidViewModel(application) {
    private val firebaseRepository: FirebaseRepository
    private var currentUserObject: MutableLiveData<UserObject>
    private var category: MutableLiveData<String> = MutableLiveData("카테고리 선택")

    var userId: String? = null
    var userLocation: String? = null
    var imageList: ArrayList<String> = arrayListOf()
    var uriImageList: MutableLiveData<ArrayList<Uri>> = MutableLiveData(arrayListOf())
    var title: String = ""
    var overview: String = ""
    var lookup: Long = 0
    var price: String = ""
    var viewPagerPosition = MutableLiveData(0)
    var uriImageListSize = MutableLiveData(0)




    init {
        firebaseRepository = FirebaseRepository.getInstance()
        currentUserObject = firebaseRepository.getCurretUser()
        userId = currentUserObject.value!!.userId
        userLocation = currentUserObject.value!!.location
        category = firebaseRepository.getcategory()
    }


    fun setCategory(selectedCategory: String) {
        firebaseRepository.setCategory(selectedCategory)
    }


    fun removeFromUriList() {
        if (uriImageList.value.isNullOrEmpty()) return
        uriImageList.value!!.removeAt(viewPagerPosition.value!! - 1)
        uriImageList.value = uriImageList.value
        uriImageListSize.value = uriImageList.value!!.size
    }

    fun getisWirteSuccess() = firebaseRepository.getisWriteSucess()

    fun getcategory() = category

    fun addUriToList(uri: Uri) {
        if (uriImageList.value!!.size >= 10) return
        uriImageList.value!!.add(uri)
        uriImageListSize.value = uriImageList.value!!.size
    }

    fun commitItemObject() {
        viewModelScope.launch(Dispatchers.IO) {
            uriImageList.value!!.forEachIndexed { index, uri ->
                imageList.add(firebaseRepository.firebaseStorageInsertItemImage(uri))
            }
            var itemRef = firebaseRepository.commitItemObject(imageList, title, overview, price)
            firebaseRepository.addItemList(itemRef)
        }
    }

    fun getUriList() = uriImageList

    fun test() {
    }

    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return WriteViewModel(application) as T
        }
    }


}