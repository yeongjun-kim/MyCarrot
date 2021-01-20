package com.mvvm.mycarrot.room

import android.app.Application
import androidx.lifecycle.*
import com.mvvm.mycarrot.repository.FirebaseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LocationViewModel(application: Application) : AndroidViewModel(application) {
    private var locationRepository: LocationRepository
    private var firebaseRepository: FirebaseRepository

    private var locationList: LiveData<MutableList<Location>>
    private var searchList = MutableLiveData<List<Location>>()
    private var selectedLocation: Location? = null
    var currentLat = 0.toDouble()
    var currentLong = 0.toDouble()

    var searchString = MutableLiveData("")


    init {
        locationRepository = LocationRepository(application)
        firebaseRepository = FirebaseRepository.getInstance()
        locationList = locationRepository.getAll()
        searchList = locationRepository.getLikeQuery()
    }

    fun getAll() = locationList


    fun setLikeQuery() {
        viewModelScope.launch(Dispatchers.IO) {
            locationRepository.setLikeQuery(searchString.value?.trimStart()?.trim())
        }
    }

    fun setLocationQuery() {
        viewModelScope.launch(Dispatchers.IO) {
            locationRepository.setLocationQuery(currentLat, currentLong)
        }
    }

    fun setCurrentLatLong(lat: Double, long: Double) {
        currentLat = lat
        currentLong = long

        firebaseRepository.setCurrentLatLong(currentLat, currentLong)
    }

    fun getSelectedLocation() = selectedLocation

    fun setSelectedLocation(location: Location) {
        selectedLocation = location
    }

    fun getLikeQuery() = searchList

    class Factory(val application: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            return LocationViewModel(application) as T
        }
    }
}