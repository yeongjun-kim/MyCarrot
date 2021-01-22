package com.mvvm.mycarrot.model

import com.google.firebase.firestore.GeoPoint
import kotlinx.android.parcel.RawValue


/**
 *
 * id: Item Id (fireabaseStore document Id)
 * userId: Item Owner Id (firebaseAuth Id)
 * userNickname: Item Owner Nickname
 * userLocation: Item Owner Location (OO구 OO동)
 * status: sell, soldout, reservation
 * imageList: Item Image Url List (firebaseStore url)
 * title: Item Title
 * category: Item Category
 * overview: Item Overview
 * lookup: Item Lookup Count
 * price: Item Price
 * geoPoint: Item Owner GeoPoint(latitude, longitude)
 * timestamp: Item enroll millisecond
 * likeCount: Item Like count
 * chatList: Item Chat UserList
 *
 */
data class ItemObject(
    var id: String? = null,
    var userId: String? = null,
    var userNickname: String? = null,
    var userLocation: String? = null,
    var status: String = "sell",
    var imageList: ArrayList<String> = arrayListOf(),
    var title: List<String> = listOf(),
    var category: String? = null,
    var overview: String? = null,
    var lookup: Long = 0,
    var price: String = "0",
    var geoPoint: @RawValue GeoPoint = GeoPoint(37.55, 126.97), // Default 관악구
    var timestamp: Long? = null,
    var likeCount: Long = 0,
    var chatList: ArrayList<String> = arrayListOf()
)


