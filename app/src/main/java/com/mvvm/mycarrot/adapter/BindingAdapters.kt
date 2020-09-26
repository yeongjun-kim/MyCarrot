package com.mvvm.mycarrot.adapter

import android.text.Spannable
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.set
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.mvvm.mycarrot.R
import java.text.DecimalFormat

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("DecimalFormat")
    fun setDecimalFormat(view: TextView, inputString: String) {
        var formatter = DecimalFormat("###,###")
        var formattedStringPrice = formatter.format(inputString.toLong())
        view.text = "${formattedStringPrice} 원"
    }

    /**
     * ImageView
     */
    @JvmStatic
    @BindingAdapter("setImage")
    fun setImage(imageView: ImageView, progress: Int) {

        var drawable = if (progress in 0..32) R.drawable.frame_map_0
        else if (progress in 33..65) R.drawable.frame_map_1
        else if (progress in 66..99) R.drawable.frame_map_2
        else R.drawable.frame_map_3

        Glide.with(imageView.context)
            .load(drawable)
            .thumbnail(0.1f)
            .into(imageView)
    }

    /**
     * Item 의 대표(첫번째) url ImageView
     */
    @JvmStatic
    @BindingAdapter("FirstImage")
    fun setFirstImage(imageView: ImageView, url: String) {

        Glide.with(imageView.context)
            .load(url)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(14)))
            .thumbnail(0.1f)
            .into(imageView)
    }

    /**
     * 근처 동네 N개 밑줄 TextView ( SetupTownActivity)
     */
    @JvmStatic
    @BindingAdapter("RangeText")
    fun setRangeText(textView: TextView, progress: Int) {
        var text = if (progress in 0..32) SpannableString("근처 동네 10개")
        else if (progress in 33..65) SpannableString("근처 동네 30개")
        else if (progress in 66..99) SpannableString("근처 동네 50개")
        else SpannableString("근처 동네 70개")
        text.setSpan(UnderlineSpan(), 0, text.length, 0)

        textView.text = text
    }

    /**
     * OO구 OO동 에서 동만 나오게 하는 TextView (HomeFragment)
     */
    @JvmStatic
    @BindingAdapter("DongText")
    fun setDongText(textView: TextView, location: String) {
        textView.text = location.split(" ")[1]
    }

    /**
     * N일전 TextView (HomeFragment)
     */
    @JvmStatic
    @BindingAdapter("BeforeDay")
    fun setBeforeDay(textView: TextView, itemMilli: Long) {
        var day = (System.currentTimeMillis() - itemMilli)/86400000
        textView.text = "${day.toString()}일 전"
    }

}