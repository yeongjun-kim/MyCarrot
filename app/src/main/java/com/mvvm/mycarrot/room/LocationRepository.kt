package com.mvvm.mycarrot.room

import android.app.Application
import androidx.lifecycle.LiveData

class LocationRepository(application: Application) {
    private var locationDatabase: AppDatabase
    private val locationDao: LocationDao
    private var locationList: LiveData<MutableList<Location>>

    init {
        locationDatabase = AppDatabase.getInstance(application)
        locationDao = locationDatabase.locationDao()
        locationList = locationDao.getAll()
    }

    fun getAll() = locationList
    fun insert(location:Location){
        locationDao.insert(location)
    }
}