package com.mvvm.mycarrot.model

import com.google.firebase.firestore.GeoPoint
import kotlinx.android.parcel.RawValue

data class ItemObject(
    var id:String? = null,
    var userId: String? = null,
    var userNickname:String?=null,
    var userLocation: String? = null,
    var status:String = "sell",
    var imageList: ArrayList<String> = arrayListOf(),
    var title: List<String> = listOf(),
    var category: String? = null,
    var overview: String? = null,
    var lookup: Long = 0,
    var price: String = "0",
    var geoPoint: @RawValue GeoPoint = GeoPoint(37.55,126.97), // Default 관악구
    var timestamp:Long?= null,
    var likeCount:Long = 0
)


