package com.mvvm.mycarrot.fcm


data class NotificationBody(
    val to:String,
    var data: NotificationData
)