package com.mvvm.mycarrot.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao{
    @Query("SELECT * FROM location")
    fun getAll():LiveData<MutableList<Location>>

    @Insert
    fun insert(location:Location)
}