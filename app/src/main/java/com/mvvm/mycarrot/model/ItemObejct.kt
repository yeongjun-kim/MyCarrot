package com.mvvm.mycarrot.model

data class ItemObejct(
    var userId: String? = null,
    var userLocation: String? = null,
    var imageList: ArrayList<String> = arrayListOf(),
    var title: String? = null,
    var category: String? = null,
    var overview: String? = null,
    var lookup: Long = 0,
    var price: String = "0"
//    var seller: UserObject? = null
)
