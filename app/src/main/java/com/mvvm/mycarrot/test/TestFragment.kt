package com.mvvm.mycarrot.test

import android.content.Context
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator

import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.view.navigation.SearchFragment
import kotlinx.android.synthetic.main.activity_test.*

class TestFragment : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_test, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        var a = ViewPagerAdapter(activity!!)
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
