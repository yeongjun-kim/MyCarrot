package com.mvvm.mycarrot.model

import com.google.firebase.firestore.GeoPoint

data class ItemObject(
    var userId: String? = null,
    var userLocation: String? = null,
    var imageList: ArrayList<String> = arrayListOf(),
    var title: String? = null,
    var category: String? = null,
    var overview: String? = null,
    var lookup: Long = 0,
    var price: String = "0",
    var geoPoint:GeoPoint = GeoPoint(37.55,126.97), // Default 관악구
    var timestamp:Long?= null
//    var seller: UserObject? = null
)
