package com.mvvm.mycarrot.model

data class ItemObejct(
    var itemId: String? = null,
    var imageList: ArrayList<String> = arrayListOf(),
    var title: String? = null,
    var categoryList: ArrayList<String> = arrayListOf(),
    var overview: String? = null,
    var lookup: Long = 0,
    var seller: UserObject? = null
)
