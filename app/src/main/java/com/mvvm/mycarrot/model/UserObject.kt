package com.mvvm.mycarrot.model

import com.google.firebase.firestore.GeoPoint

data class UserObject(
    var userId: String? = null,
    var nickname: String? = null,
    var profileUrl: String? = null,
    var temperature: Double? = null,
    var location: String? = null,
    var locationCertification: Int = 0,
    var joined: Long? = null,
    var lastLoginTime: Long? = null,
    var geoPoint: GeoPoint = GeoPoint(37.55, 126.97), // Default 관악구
    var positive_1: Int = 0,
    var positive_2: Int = 0,
    var positive_3: Int = 0,
    var positive_4: Int = 0,
    var negative_1: Int = 0,
    var negative_2: Int = 0,
    var negative_3: Int = 0,
    var negative_4: Int = 0,
    var buyList: ArrayList<String> = arrayListOf(),
    var likeUserList: ArrayList<String> = arrayListOf(),
    var itemList: ArrayList<String> = arrayListOf(),
    var likeList: ArrayList<String> = arrayListOf()
)
