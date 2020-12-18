package com.mvvm.mycarrot.model

import java.io.Serializable

class LatestMessageDTO(
    val myUid: String,
    val yourUid: String,
    val itemUid: String,
    val yourNickname: String,
    val yourProfileUrl: String,
    val yourLocation: String,
    val yourTemperature: Double,
    val itemName: String,
    val itemPrice: String,
    val itemImageUrl: String,
    val timestamp: Long,
    val message: String,
    val messageType: String
) : Serializable {
    constructor() : this("", "", "", "", "", "", 36.5, "", "", "", 0, "", "")
}