package com.mvvm.mycarrot.presentation.home

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ActivitySetupTownBinding

class SetupTownActivity : AppCompatActivity() {


    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: ActivitySetupTownBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
        initBinding()
        initStatusBar()

    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_setup_town)
        binding.apply {
            vm = homeViewModel
            av = this@SetupTownActivity
            lifecycleOwner = this@SetupTownActivity
        }

    }

    private fun initViewModel() {
        homeViewModel = ViewModelProvider(
            this, HomeViewModel.Factory(application)
        ).get(HomeViewModel::class.java)

        homeViewModel.progress.observe(this, Observer { progress ->
            homeViewModel.setExtraArrange(progress)
            homeViewModel.setHomeItems()
        })
    }

    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }
}