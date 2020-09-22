package com.mvvm.mycarrot.view

import android.util.Log
import android.widget.TextView
import androidx.databinding.BindingAdapter
import java.text.DecimalFormat

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("DecimalFormat")
    fun setDecimalFormat(view: TextView, inputString:String){
        var formatter = DecimalFormat("###,###")
        var formattedStringPrice = formatter.format(inputString.toInt())
        view.text = formattedStringPrice.toString()
    }
}