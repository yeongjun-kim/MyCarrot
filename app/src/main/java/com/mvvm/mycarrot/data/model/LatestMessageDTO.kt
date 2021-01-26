package com.mvvm.mycarrot.data.model

import java.io.Serializable

class LatestMessageDTO(
    val myUid: String,
    val yourUid: String,
    val itemUid: String,
    val opponentNickname: String,
    var opponentProfileUrl: String,
    val opponentLocation: String,
    val opponentTemperature: Double,
    val itemName: String,
    val itemPrice: String,
    val itemImageUrl: String,
    val timestamp: Long,
    val message: String,
    val messageType: String
) : Serializable {
    constructor() : this("", "", "", "", "", "", 36.5, "", "", "", 0, "", "")
}