package com.mvvm.mycarrot.room

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class LocationRepository(application: Application) {
    private var locationDatabase: AppDatabase
    private val locationDao: LocationDao
    private var locationList: LiveData<MutableList<Location>>
    private var searchList= MutableLiveData<List<Location>>()

    init {
//        locationDatabase = AppDatabase.getInstance(application)
        locationDatabase = DatabaseCopier.getAppDataBase(application)!!
        locationDao = locationDatabase.locationDao()
        locationList = locationDao.getAll()
//        searchList.value = locationDao.getLikeQuery(null)
        searchList.postValue(listOf())
    }

    fun getAll() = locationList

    fun setLikeQuery(search:String?){
        var a = locationDao.getLikeQuery(search)
        Log.d("fhrm", "LocationRepository -setLikeQuery(),    a.size: ${a.size}")
        searchList.postValue(a)
    }

    fun getLikeQuery() = searchList


    fun insert(location: Location) {
        locationDao.insert(location)
    }
}