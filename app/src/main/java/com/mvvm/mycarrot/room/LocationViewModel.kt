package com.mvvm.mycarrot.room

import android.app.Application
import android.util.Log
import androidx.lifecycle.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private var locationRepository: LocationRepository
    private var locationList: LiveData<MutableList<Location>>

    init {
        locationRepository = LocationRepository(application)
        locationList = locationRepository.getAll()
    }

    fun getAll() = locationList

    fun insert(location: Location) {
        viewModelScope.launch(Dispatchers.IO) {
            locationRepository.insert(location)
        }
    }

    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LocationViewModel(application) as T
        }
    }
}