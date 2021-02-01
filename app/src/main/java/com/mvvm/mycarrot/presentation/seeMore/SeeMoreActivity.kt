package com.mvvm.mycarrot.presentation.seeMore

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ActivitySeeMoreBinding
import com.mvvm.mycarrot.presentation.home.HomeViewModel

class SeeMoreActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySeeMoreBinding
    private lateinit var homeViewModel: HomeViewModel
    private val seeMoreTotalFragment = SeeMoreTotalFragment()
    private val seeMoreForSaleFragment = SeeMoreForSaleFragment()
    private val seeMoreSoldOutFragment = SeeMoreSoldOutFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
        initBinding()
        initTabLayoutViewPager()
        initStatusBar()

    }

    private fun initViewModel() {
        homeViewModel = ViewModelProvider(
            this, HomeViewModel.Factory(this.application)
        ).get(HomeViewModel::class.java)
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_see_more)
        binding.apply {
            lifecycleOwner = this@SeeMoreActivity
            av = this@SeeMoreActivity
        }
    }

    private fun initTabLayoutViewPager() {
        val vpAdapter = ViewPagerAdapter(this)

        binding.seeMoreVp.adapter = vpAdapter

        TabLayoutMediator(binding.seeMoreTl, binding.seeMoreVp) { tab, position ->
            val tabLayoutTextArray = arrayOf("전체", "판매중", "거래완료")
            tab.text = tabLayoutTextArray[position]
        }.attach()
    }


    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }

    private inner class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> seeMoreTotalFragment
                1 -> seeMoreForSaleFragment
                2 -> seeMoreSoldOutFragment
                else -> seeMoreTotalFragment
            }
        }

        override fun getItemCount(): Int = 3
    }
}

