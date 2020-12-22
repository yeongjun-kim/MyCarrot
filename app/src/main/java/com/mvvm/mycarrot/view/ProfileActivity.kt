package com.mvvm.mycarrot.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ActivityProfileBinding
import com.mvvm.mycarrot.viewModel.HomeViewModel
import com.skydoves.balloon.*
import kotlinx.android.synthetic.main.activity_profile.*

class ProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfileBinding
    lateinit var homeViewModel: HomeViewModel
    lateinit var balloon:Balloon

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        homeViewModel = ViewModelProvider(
            this,
            HomeViewModel.Factory(application)
        ).get(HomeViewModel::class.java)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_profile)
        binding.apply {
            lifecycleOwner = this@ProfileActivity
            av = this@ProfileActivity
            vm = homeViewModel
        }

        initOtherItemList()
        initStatusBar()
        initBallon()

    }

    fun openBallon(){
        profile_iv_information.showAlignBottom(balloon,100,10)
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
            .setText("매너온도는 당근마켓 사용자로부터\n" +
                    "받은 칭찬, 후기, 비매너 평가,\n" +
                    "운영자 징계 등을 종합해서 만든\n" +
                    "매너 지표입니당.")
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