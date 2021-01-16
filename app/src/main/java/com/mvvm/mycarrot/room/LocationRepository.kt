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
        locationDatabase = DatabaseCopier.getAppDataBase(application)!!
        locationDao = locationDatabase.locationDao()
        locationList = locationDao.getAll()
        searchList.postValue(listOf())
    }

    fun getAll() = locationList

    fun setLikeQuery(search:String?){
        searchList.postValue(listOf())
        var a = locationDao.getLikeQuery(search)
        searchList.postValue(a)
    }

    fun setLocationQuery(lat:Double, long:Double){
        searchList.postValue(listOf())
        var a = locationDao.getLocationQuery(lat-0.05, lat+0.05, long-0.05, long+0.05)
        searchList.postValue(a)
    }

    fun getLikeQuery() = searchList

}