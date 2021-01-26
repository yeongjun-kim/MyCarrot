package com.mvvm.mycarrot.data.model

class MessageDTO(
    val myUid: String,
    val yourUid: String,
    val opponentProfileUrl: String,
    val timestamp: Long,
    val messageId: String,
    val message: String,
    val messageType: String
) {
    constructor() : this("", "", "", 0, "", "", "")
}