package com.mvvm.mycarrot.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LocationDao {
    @Query("SELECT * FROM locationList")
    fun getAll(): LiveData<MutableList<Location>>

//    @Query("SELECT * FROM locationList WHERE latitude = :search ")
//    fun getLikeQuery(search:String?):List<Location>

    @Query("SELECT * FROM locationList WHERE str LIKE '%'|| :search || '%'")
    fun getLikeQuery(search: String?): List<Location>


    @Insert
    fun insert(location: Location)
}