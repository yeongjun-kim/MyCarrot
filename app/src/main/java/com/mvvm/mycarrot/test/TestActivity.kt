package com.mvvm.mycarrot.test

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.HorizonSpacingItemDecoration
import com.mvvm.mycarrot.adapter.OwnerItemRvAdapterHorizontal
import com.mvvm.mycarrot.viewModel.FirebaseViewModel
import com.mvvm.mycarrot.viewModel.HomeViewModel
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {

    var uri: Uri? = null
    lateinit var viewModel: FirebaseViewModel
    lateinit var homeViewModel: HomeViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        //********************************** DEFAULT **********************************//


        viewModel = ViewModelProvider(
            this,
            FirebaseViewModel.Factory(application)
        ).get(FirebaseViewModel::class.java)

        homeViewModel = ViewModelProvider(
            this,
            HomeViewModel.Factory(application)
        ).get(HomeViewModel::class.java)

        test_btn.setOnClickListener {

        }

        // *************************************************************************** //


        var a = ViewPagerAdapter(this)
        test_vp.adapter = a
        initTabLayoutViewPager()


    }

    private fun initTabLayoutViewPager() {
        TabLayoutMediator(test_tl, test_vp) { tab, position ->
            val tabLayoutTextArray = arrayOf("Deck", "Info")
            tab.text = tabLayoutTextArray[position]
        }.attach()
    }


    private inner class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> fm1()
                1 -> fm2()
                else -> fm2()
            }
        }

        override fun getItemCount(): Int = 2
    }


}
