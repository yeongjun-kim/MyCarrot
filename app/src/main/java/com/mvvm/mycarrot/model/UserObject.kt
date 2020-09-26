package com.mvvm.mycarrot.model

import com.google.firebase.firestore.GeoPoint

data class UserObject(
    var userId: String? = null,
    var nickname: String? = null,
    var profileUrl: String? = null,
    var location: String? = null,
    var geoPoint: GeoPoint = GeoPoint(37.55,126.97), // Default 관악구
    var itemList: ArrayList<String> = arrayListOf()
)