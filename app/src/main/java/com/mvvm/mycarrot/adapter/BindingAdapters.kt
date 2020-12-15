package com.mvvm.mycarrot.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.like.LikeButton
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.model.UserObject
import java.text.DecimalFormat
import java.text.SimpleDateFormat

object BindingAdapters {
    @JvmStatic
    @BindingAdapter("DecimalFormat")
    fun setDecimalFormat(view: TextView, inputString: String) {
        var formatter = DecimalFormat("###,###")
        var formattedStringPrice = formatter.format(inputString.toLong())
        view.text = "${formattedStringPrice} 원"
    }

    @JvmStatic
    @BindingAdapter("TempeartureImage")
    fun setTempeartureImage(iv: ImageView, temp: Double) {

        var drawable = when (temp) {
            in 0.0..31.9 -> R.drawable.ic_temp1
            in 32.0..36.4 -> R.drawable.ic_temp2
            in 36.5..39.9 -> R.drawable.ic_temp3
            in 40.0..49.9 -> R.drawable.ic_temp4
            in 50.0..59.9 -> R.drawable.ic_temp5
            in 60.0..100.0 -> R.drawable.ic_temp6
            else -> R.drawable.ic_temp6
        }

        Glide.with(iv.context)
            .load(drawable)
            .thumbnail(0.1f)
            .into(iv)
    }

    @JvmStatic
    @BindingAdapter("SeekBarProgress")
    fun setSeekBarProgress(sb: SeekBar, temp: Double) {
        sb.max = 100
        sb.progress = temp.toInt()
        when (temp) {
            in 0.0..31.9 -> sb.progressTintList =
                ColorStateList.valueOf(Color.parseColor("#0F2036"))
            in 32.0..36.4 -> sb.progressTintList =
                ColorStateList.valueOf(Color.parseColor("#1C3A62"))
            in 36.5..39.9 -> sb.progressTintList =
                ColorStateList.valueOf(Color.parseColor("#376EBA"))
            in 40.0..49.9 -> sb.progressTintList =
                ColorStateList.valueOf(Color.parseColor("#60AF59"))
            in 50.0..59.9 -> sb.progressTintList =
                ColorStateList.valueOf(Color.parseColor("#F2B040"))
            in 60.0..100.0 -> sb.progressTintList =
                ColorStateList.valueOf(Color.parseColor("#E46F2E"))
        }
    }


    @JvmStatic
    @BindingAdapter("TempeartureText")
    fun setTempeartureText(view: TextView, temp: Double) {
        view.text = "${temp}°C"
        when (temp) {
            in 0.0..31.9 -> view.setTextColor(Color.parseColor("#0F2036"))
            in 32.0..36.4 -> view.setTextColor(Color.parseColor("#1C3A62"))
            in 36.5..39.9 -> view.setTextColor(Color.parseColor("#376EBA"))
            in 40.0..49.9 -> view.setTextColor(Color.parseColor("#60AF59"))
            in 50.0..59.9 -> view.setTextColor(Color.parseColor("#F2B040"))
            in 60.0..100.0 -> view.setTextColor(Color.parseColor("#E46F2E"))
        }
    }

    @JvmStatic
    @BindingAdapter("ProfileItemList")
    fun setProfileItemList(view: TextView, user: UserObject) {
        view.text = "판매상품 ${user.itemList.size}개"
    }

    @JvmStatic
    @BindingAdapter("UserId")
    fun setUserId(view: TextView, userId: String) {
        view.text = "#${userId.substring(0, 7)}"
    }

    @JvmStatic
    @BindingAdapter("ProfileInfo")
    fun setProfileInfo(view: TextView, user: UserObject) {
        var nWeek =
            ((System.currentTimeMillis() - user.lastLoginTime!!) / (1000 * 60 * 60 * 24 * 7)) + 1
        var dong = user.location!!.split(" ")[1]
        var sdf = SimpleDateFormat("yyyy년 MM월 dd일").format(user.joined)


        view.text = "${dong} ${user.locationCertification}회 인증\n" +
                "${sdf} 가입 (최근 ${nWeek}주 이내 활동)"
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
        if (likeList.isNullOrEmpty() || itemId.isNullOrBlank()) return
        likeButton.isLiked = likeList!!.contains(itemId)
    }


}