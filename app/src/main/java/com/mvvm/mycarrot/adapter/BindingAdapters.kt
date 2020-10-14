package com.mvvm.mycarrot.adapter

import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.like.LikeButton
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

    @JvmStatic
    @BindingAdapter("UserId")
    fun setUserId(view: TextView, userId: String) {
        view.text = "#${userId.substring(0,7)}"
    }


    @JvmStatic
    @BindingAdapter("Title")
    fun setTitle(view: TextView, inputArray: List<String>) {
        view.text = inputArray.joinToString(" ")
    }

    /**
     * ImageView ( SetupTownActivity 의 이미지 설정 )
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
     * ImageView ( 기본 Round imageView 세팅 )
     */
    @JvmStatic
    @BindingAdapter("RepresentationcircleSrc")
    fun setRepresentationCircleSrc(imageView: ImageView, url: String?) {

        Glide.with(imageView.context)
            .load(url)
            .placeholder(R.drawable.ic_user)
            .circleCrop()
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
    fun setDongText(textView: TextView, location: String?) {
        if (!location.isNullOrBlank()) textView.text = location.split(" ")[1]

    }

    /**
     * N일전 TextView (HomeFragment)
     */
    @JvmStatic
    @BindingAdapter("BeforeDay")
    fun setBeforeDay(textView: TextView, itemMilli: Long) {
        var day = (System.currentTimeMillis() - itemMilli) / 86400000
        textView.text = "${day}일 전"
    }

    /**
     * 조회 TextView (ItemActivity)
     */
    @JvmStatic
    @BindingAdapter("LookUp")
    fun setLookUp(textView: TextView, lookup: Long) {
        textView.text = "조회 ${lookup}"
    }

    /**
     * 좋아요 누른 상품이라면 isLiked true (ItemActivity)
     */
    @JvmStatic
    @BindingAdapter(value = ["likeList", "itemId"], requireAll = false)
    fun setLiked(likeButton: LikeButton, likeList: ArrayList<String>?, itemId: String?) {
        if(likeList.isNullOrEmpty() || itemId.isNullOrBlank()) return
        likeButton.isLiked = likeList!!.contains(itemId)
    }


}