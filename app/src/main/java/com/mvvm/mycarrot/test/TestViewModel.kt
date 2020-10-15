package com.mvvm.mycarrot.viewModel

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.mvvm.mycarrot.model.ItemObject
import com.mvvm.mycarrot.model.UserObject
import com.mvvm.mycarrot.repository.FirebaseRepository
import com.mvvm.mycarrot.test.TestObject

class TestViewModel(application: Application) : AndroidViewModel(application) {


    private val firebaseRepository: FirebaseRepository
    private val firebaseStore: FirebaseFirestore
    private var currentUserObject: MutableLiveData<UserObject>


    init {
        firebaseRepository = FirebaseRepository.getInstance()
        firebaseStore = FirebaseFirestore.getInstance()
        currentUserObject = firebaseRepository.getCurretUser()
    }


    fun test() {
        var minGeoPoint =firebaseRepository.getMinGeoPoint()
        var maxGeoPoint =firebaseRepository.getMaxGeoPoint()


        firebaseStore.collection("items")
            .whereGreaterThanOrEqualTo("geoPoint", minGeoPoint)
            .whereLessThanOrEqualTo("geoPoint", maxGeoPoint)
            .whereArrayContains("title","아이폰")
            .orderBy("geoPoint")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .limit(10)
            .get()
            .addOnSuccessListener { result ->
                var a = result.map { it.toObject(ItemObject::class.java) }
                a.forEachIndexed { index, item ->
                    Log.d("fhrm", "TestViewModel -test(),    index: ${index}, item: ${item.title}")
                }
            }

    }


    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return TestViewModel(application) as T
        }
    }


}

