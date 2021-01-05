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
    var likeUserList: ArrayList<String> = arrayListOf(),
    var itemList: ArrayList<String> = arrayListOf(),
    var likeList: ArrayList<String> = arrayListOf()
)

/**
 *
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 * 상대방 프로필 누른당, 모아보기 누르면 likeUserList 에 추가하는것부터 작업 하면됨.
 * 그런다음 MyFragment 에서 모아보기 누르면 해당 애들 아이템리스트 show 해주면 됨.
 *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * * *
 *
 *
 *
 **/