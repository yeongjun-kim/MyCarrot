package com.mvvm.mycarrot.model

class MessageDTO(
    val myUid: String,
    val yourUid: String,
    val yourProfileUrl:String,
    val timestamp: Long,
    val messageId: String,
    val message: String,
    val messageType: String
) {
    constructor() : this("", "","", 0, "", "", "")
}