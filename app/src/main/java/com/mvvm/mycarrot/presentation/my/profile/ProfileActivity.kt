package com.mvvm.mycarrot.presentation.my.profile

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.recyclerView.ItemRvMannerAdapter
import com.mvvm.mycarrot.databinding.ActivityProfileBinding
import com.mvvm.mycarrot.presentation.seeMore.SeeMoreActivity
import com.mvvm.mycarrot.presentation.home.HomeViewModel
import com.skydoves.balloon.ArrowOrientation
import com.skydoves.balloon.Balloon
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.showAlignBottom
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProfileBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var balloon: Balloon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        initViewModel()
        initBinding()
        initOtherItemList()
        initStatusBar()
        initBallon()
        initRv()


    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        binding.apply {
            lifecycleOwner = this@ProfileActivity
            av = this@ProfileActivity
            vm = homeViewModel
        }
    }

    private fun initViewModel() {
        homeViewModel = ViewModelProvider(
            this,
            HomeViewModel.Factory(application)
        ).get(HomeViewModel::class.java)
    }

    private fun initRv() {
        var mannerList = listOf(
            Pair(homeViewModel.getselectedItemOwner().positive_1, getString(R.string.positive_1)),
            Pair(homeViewModel.getselectedItemOwner().positive_2, getString(R.string.positive_2)),
            Pair(homeViewModel.getselectedItemOwner().positive_3, getString(R.string.positive_3)),
            Pair(homeViewModel.getselectedItemOwner().positive_4, getString(R.string.positive_4))
        )

        var mAdapter =
            ItemRvMannerAdapter(mannerList.filter { it.first != 0 })

        binding.profileRv.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this.context)
            adapter = mAdapter
        }
    }

    fun likeUser() {
        val targetUid = homeViewModel.getselectedItemOwner().userId

        if (homeViewModel.getCurrentUserObject().value!!.likeUserList.contains(targetUid)) {
            homeViewModel.deleteFromLikeUserList(targetUid!!)
            changeButtonToContain("not contain")
        } else {
            homeViewModel.addToLikeUserList(targetUid!!)
            changeButtonToContain("contain")
        }

    }

    fun changeButtonToContain(mode: String) {
        val btn = binding.profileBtnSum

        if (mode == "contain") {
            btn.background =
                ContextCompat.getDrawable(btn.context, R.drawable.bg_custom_textview_orange)
            btn.text = "모아보는중"
            btn.setTextColor(Color.parseColor("#ffffff"))
        } else if (mode == "not contain") {
            btn.background = ContextCompat.getDrawable(btn.context, R.drawable.bg_custom_textview)
            btn.text = "모아보기"
            btn.setTextColor(Color.parseColor("#000000"))
        }
    }

    fun openBallon() {
        profile_iv_information.showAlignBottom(balloon, 100, 10)
    }

    fun startActivityMannerDetail() {
        startActivity(
            Intent(
                this, MannerDetailActivity
                ::class.java
            )
        )
    }

    fun startActivitySeeMore() {
        homeViewModel.saveSelectedItemOwnersItem()
        startActivity(Intent(this, SeeMoreActivity::class.java))
    }

    private fun initOtherItemList() {
        homeViewModel.setSelectedItemOwnersItem()
    }


    private fun initBallon() {
        balloon = Balloon.Builder(this)
            .setArrowSize(10)
            .setArrowOrientation(ArrowOrientation.TOP)
            .setArrowPosition(0.2f)
            .setHeight(130)
            .setWidth(200)
            .setCornerRadius(8f)
            .setText(
                "매너온도는 당근마켓 사용자로부터\n" +
                        "받은 칭찬, 후기, 비매너 평가,\n" +
                        "운영자 징계 등을 종합해서 만든\n" +
                        "매너 지표입니당."
            )
            .setTextColor(ContextCompat.getColor(this, R.color.white))
            .setBackgroundColorResource(R.color.colorPrimary)
            .setBalloonAnimation(BalloonAnimation.FADE)
            .build()
    }


    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }
}