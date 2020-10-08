package com.mvvm.mycarrot.view.navigation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.GridSpacingItemDecoration
import com.mvvm.mycarrot.adapter.HorizonSpacingItemDecoration
import com.mvvm.mycarrot.adapter.OwnerItemRvAdapter
import com.mvvm.mycarrot.adapter.OwnerItemRvAdapterHorizontal
import com.mvvm.mycarrot.databinding.FragmentSearchBinding
import com.mvvm.mycarrot.databinding.FragmentSeemoreBinding
import com.mvvm.mycarrot.model.ItemObject
import com.mvvm.mycarrot.test.fm1
import com.mvvm.mycarrot.test.fm2
import com.mvvm.mycarrot.view.CustomProgressDialog
import com.mvvm.mycarrot.view.ItemActivity
import com.mvvm.mycarrot.viewModel.HomeViewModel
import com.mvvm.mycarrot.viewModel.SearchViewModel
import kotlinx.android.synthetic.main.activity_test.*


class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    lateinit var searchViewModel: SearchViewModel
    var searchTradingFragment = SearchTradingFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        binding.testBtn3.setOnClickListener {
            searchViewModel.test()
        }



        binding.apply {
            fm = this@SearchFragment
        }

        initTabLayoutViewPager()
    }

    private fun initTabLayoutViewPager() {
        val vpAdapter =ViewPagerAdapter(activity!!)
        binding.fmSearchVp.adapter = vpAdapter

        TabLayoutMediator(binding.fmSearchTl, binding.fmSearchVp) { tab, position ->
            val tabLayoutTextArray = arrayOf("중고거래", "사람")
            tab.text = tabLayoutTextArray[position]
        }.attach()
     }

    private inner class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {
        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> searchTradingFragment
                1 -> fm2()
                else -> fm2()
            }
        }

        override fun getItemCount(): Int = 2
    }
}
