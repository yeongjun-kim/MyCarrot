package com.mvvm.mycarrot.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ActivitySellListBinding

class SellListActivity : AppCompatActivity() {

    lateinit var binding: ActivitySellListBinding
    var sellListForSaleFragment = SellListForSaleFragment()
    var sellListSoldOutFragment = SellListSoldOutFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_sell_list)
        initTabLayoutViewPager()
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
}

/**

 SellListActivity의 TabLayout(forsaleFragment, soldoutFragment) 구현부터 시작하면됨.










 **/
