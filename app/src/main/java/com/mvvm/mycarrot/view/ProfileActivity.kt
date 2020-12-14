package com.mvvm.mycarrot.view

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ActivityProfileBinding
import com.mvvm.mycarrot.viewModel.HomeViewModel

class ProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityProfileBinding
    lateinit var homeViewModel: HomeViewModel

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


        initStatusBar()

    }


    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }
}