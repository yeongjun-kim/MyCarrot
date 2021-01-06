package com.mvvm.mycarrot.view.navigation

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ActivitySetupTownBinding
import com.mvvm.mycarrot.databinding.FragmentHomeBinding
import com.mvvm.mycarrot.viewModel.HomeViewModel
import com.mvvm.mycarrot.viewModel.WriteViewModel
import kotlinx.android.synthetic.main.activity_setup_town.*

class SetupTownActivity : AppCompatActivity() {


    lateinit var homeViewModel: HomeViewModel
    lateinit var binding: ActivitySetupTownBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_setup_town)
        homeViewModel = ViewModelProvider(
            this, HomeViewModel.Factory(application)
        ).get(HomeViewModel::class.java)

        binding.apply {
            vm = homeViewModel
            av = this@SetupTownActivity
            lifecycleOwner = this@SetupTownActivity
        }


        homeViewModel.progress.observe(this, Observer {progress ->
            homeViewModel.setExtraArrange(progress)
            homeViewModel.setHomeItems()
        })

        initStatusBar()

    }

    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }
}