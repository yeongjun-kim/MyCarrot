package com.mvvm.mycarrot.adapter

import android.content.res.ColorStateList
import android.graphics.Color
import android.text.SpannableString
import android.text.style.UnderlineSpan
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.SeekBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.view.marginStart
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.like.LikeButton
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.model.ItemObject
import com.mvvm.mycarrot.model.UserObject
import com.mvvm.mycarrot.viewModel.HomeViewModel
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

    /*
    OOO님과 OO를 거래했어요. (SendReviewFinishFragment)
     */
    @JvmStatic
    @BindingAdapter("ReviewTextThree")
    fun setReviewTextThree(tv: TextView, nickname: String?) {
        tv.text = "${nickname}님에게 따뜻한 후기를 보냈어요."
    }

    /*
    OOO 님에게 따뜻한 후기를 보냈어요. (SendReviewFinishFragment)
    */
    @JvmStatic
    @BindingAdapter("ReviewTextTwo")
    fun setReviewTextTwo(tv: TextView, nickname: String?) {
        tv.text = "${nickname}님에게 따뜻한 후기를 보냈어요."
    }

    /*
    마지막 대화 지난 N주 (SelectBuyerFragment)
     */
    @JvmStatic
    @BindingAdapter("ReviewText")
    fun setReviewText(tv: TextView, nickname: String?) {
        tv.text = "${nickname}님과의 거래가 어땠나요?"
    }

    /*
    마지막 대화 지난 N주 (SelectBuyerFragment)
     */
    @JvmStatic
    @BindingAdapter("LatestMessageTime")
    fun setLatestMessageTime(tv: TextView, timestamp: Long) {
        val n = (System.currentTimeMillis() - timestamp) / 604800000
        tv.text = "마지막 대화 지난 ${n}주"
    }

    /*
    Title 앞에 아이템의 상태를 나타냄 (activity_item)
     */
    @JvmStatic
    @BindingAdapter("TvStatus")
    fun setTvStatus(tv: TextView, status: String) {
        if (status == "soldout") {
            tv.text = "거래완료  "
            tv.setTextColor(ContextCompat.getColor(tv.context, R.color.colorDarkGray))
        } else if (status == "reservation") {
            tv.text = "예약중  "
            tv.setTextColor(ContextCompat.getColor(tv.context, R.color.colorGreen))
        } else {
            tv.text = ""
            tv.setBackgroundColor(Color.TRANSPARENT)
        }
    }

    /*
    아이템 상태에 따라, 가격 TextView의 마진 추가 및 제
     */
    @JvmStatic
    @BindingAdapter("StatusMarginStart")
    fun setStatusMarginStart(tv: TextView, status: String) {

        val param = tv.layoutParams as ViewGroup.MarginLayoutParams
        if (status == "sell") {
            param.marginStart = 0
            tv.layoutParams = param
        } else {
            param.marginStart = 12
            tv.layoutParams = param
        }
    }

    /*
    아이템 상태에 따라, 이미 판매 완료 됐다면 해당 뷰를 반투명하게 만듦 (item_rv_item)
     */
    @JvmStatic
    @BindingAdapter("IsSoldout")
    fun setIsSoldout(cl: ConstraintLayout, status: String) {
        if (status == "soldout") cl.alpha = 0.4f
    }

    /*
    아이템 상태에 따라, 가격 옆에 거래완료/예약중 표시해주는것 (item_rv_item)
     */
    @JvmStatic
    @BindingAdapter("Status")
    fun setStatus(tv: TextView, status: String) {

        if (status == "sell") {
            tv.visibility = View.GONE
        } else if (status == "soldout") {
            tv.background =
                ContextCompat.getDrawable(tv.context, R.drawable.bg_custom_round_dark_gray)
            tv.setTextColor(Color.parseColor("#ffffff"))
            tv.text = "거래완료"
            tv.visibility = View.VISIBLE
        } else if (status == "reservation") {
            tv.background = ContextCompat.getDrawable(tv.context, R.drawable.bg_custom_round_green)
            tv.setTextColor(Color.parseColor("#ffffff"))
            tv.text = "예약중"
            tv.visibility = View.VISIBLE
        }
    }


    /*
    프로필 확인하는 유저가 모아보기에 추가되어있는 유저인지에 대해 Button Background 변경 (ProfileActivity)
     */
    @JvmStatic
    @BindingAdapter("LikeUserBtn")
    fun setLikeUserBtn(btn: Button, vm: HomeViewModel) {
        val targetUid = vm.getselectedItemOwner().userId
        if (vm.getCurrentUserObject().value!!.likeUserList.contains(targetUid)) {
            btn.background =
                ContextCompat.getDrawable(btn.context, R.drawable.bg_custom_textview_orange)
            btn.text = "모아보는중"
            btn.setTextColor(Color.parseColor("#ffffff"))
        } else {
            btn.background = ContextCompat.getDrawable(btn.context, R.drawable.bg_custom_textview)
            btn.text = "모아보기"
            btn.setTextColor(Color.parseColor("#000000"))
        }
    }


    /*
    현재 기기의 위치와, 등록시 위치의 차이 보여줌 (NeightborhoodCertificationActivity)
     */
    @JvmStatic
    @BindingAdapter("CertificationText")
    fun setCertificationText(view: TextView, vm: HomeViewModel) {
        var currentDong = vm.getCurrentLocation().value!!.split(" ")[1]
        var userDong = vm.getCurrentUserObject().value!!.location!!.split(" ")[1]
        var text = ""

        if (currentDong == userDong) {
            text = "현재 위치가 내 동네로 설정한 '${userDong}' 내에 있어요."
        } else {
            text = "현재 내 동네로 설정되어 있는 '${userDong}' 에서만 동네인증을 할 수 있어요. 현재 위치를 확인해주세요."
        }

        view.text = text
    }

    /*
    현재 기기의 위치와, 등록시 위치가 다르면 background, Clickable 변경 (NeightborhoodCertificationActivity)
     */
    @JvmStatic
    @BindingAdapter("IsCertificationOk")
    fun setIsCertificationOk(btn: Button, vm: HomeViewModel) {
        var currentDong = vm.getCurrentLocation().value!!.split(" ")[1]
        var userDong = vm.getCurrentUserObject().value!!.location!!.split(" ")[1]

        if (currentDong == userDong) {
            btn.background =
                ContextCompat.getDrawable(btn.context, R.drawable.bg_custom_button_orange)
            btn.isEnabled = true
        } else {
            btn.background =
                ContextCompat.getDrawable(btn.context, R.drawable.bg_custom_button_gray)
            btn.isEnabled = false
        }

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
    fun setTitle(view: TextView, inputArray: List<String>?) {
        view.text = inputArray?.joinToString(" ")
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
    fun setFirstImage(imageView: ImageView, url: String?) {
        Glide.with(imageView.context)
            .load(url)
            .apply(RequestOptions.bitmapTransform(RoundedCorners(28)))
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
     * NickName 입력이 정상적이면 완료버튼 Clickable, Color Black
     * NickName 입력이 공백이라면 완료버튼 Not Clickable, Color Gray
     */
    @JvmStatic
    @BindingAdapter("IsChangOk")
    fun setIsChangOk(textView: TextView, str: String) {
        if (str.isNullOrBlank()) {
            textView.isClickable = false
            textView.setTextColor(Color.parseColor("#E6E6E6"))
        } else {
            textView.isClickable = true
            textView.setTextColor(Color.parseColor("#000000"))
        }
    }

    /**
     * OO동 #ABCD1234 나오게 하는 TextView (MyFragment)
     */
    @JvmStatic
    @BindingAdapter("DongUid")
    fun setDongUid(textView: TextView, userObject: UserObject) {
        val dong = userObject.location!!.split(" ")[1]
        val uid = userObject.userId!!.substring(0, 9)
        textView.text = "${dong} #${uid}"
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