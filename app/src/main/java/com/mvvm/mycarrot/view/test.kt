package com.mvvm.mycarrot.view

import android.content.Context
import android.content.Intent

class test(){
    fun a(context: Context){
        var intent = Intent(context, MainActivity::class.java)
        context.startActivity(intent)
    }


}