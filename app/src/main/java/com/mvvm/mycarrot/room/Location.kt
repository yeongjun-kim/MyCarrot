package com.mvvm.mycarrot.room

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "locationList")
data class Location (
    @PrimaryKey val str: String,
    @ColumnInfo(name = "latitude") val latitude:Double?,
    @ColumnInfo(name = "longitude") val longitude:Double?
)