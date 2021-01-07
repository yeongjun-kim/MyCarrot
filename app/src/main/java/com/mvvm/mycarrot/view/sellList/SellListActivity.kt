package com.mvvm.mycarrot.view.sellList

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ActivitySellListBinding
import com.mvvm.mycarrot.viewModel.MyViewModel

class SellListActivity : AppCompatActivity() {

    lateinit var binding: ActivitySellListBinding
    lateinit var myViewModel: MyViewModel
    var sellListForSaleFragment =
        SellListForSaleFragment()
    var sellListSoldOutFragment =
        SellListSoldOutFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sell_list)
        myViewModel = ViewModelProvider(this, MyViewModel.Factory(application)).get(MyViewModel::class.java)
        binding.apply {
            lifecycleOwner = this@SellListActivity
            av = this@SellListActivity

        }



        initTabLayoutViewPager()
        initStatusBar()
        myViewModel.setmyItemList()
    }


    private fun initTabLayoutViewPager() {
        val vpAdapter = ViewPagerAdapter(this!!)

        binding.sellListVp.adapter = vpAdapter

        TabLayoutMediator(binding.sellListTl, binding.sellListVp) { tab, position ->
            val tabLayoutTextArray = arrayOf("판매중", "거래완료")
            tab.text = tabLayoutTextArray[position]
        }.attach()
    }

    private inner class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> sellListForSaleFragment
                1 -> sellListSoldOutFragment
                else -> sellListForSaleFragment
            }
        }

        override fun getItemCount(): Int = 2
    }

    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }
}
