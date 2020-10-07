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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.GridSpacingItemDecoration
import com.mvvm.mycarrot.adapter.HorizonSpacingItemDecoration
import com.mvvm.mycarrot.adapter.OwnerItemRvAdapter
import com.mvvm.mycarrot.adapter.OwnerItemRvAdapterHorizontal
import com.mvvm.mycarrot.databinding.FragmentSearchBinding
import com.mvvm.mycarrot.databinding.FragmentSeemoreBinding
import com.mvvm.mycarrot.model.ItemObject
import com.mvvm.mycarrot.view.CustomProgressDialog
import com.mvvm.mycarrot.view.ItemActivity
import com.mvvm.mycarrot.viewModel.HomeViewModel
import com.mvvm.mycarrot.viewModel.SearchViewModel


class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    lateinit var searchViewModel: SearchViewModel
    lateinit var homeViewModel: HomeViewModel // Item Click 에 대한 로직이 있어서 Click에 사용 (selectedItem)

    var hotitemRvAdapter = OwnerItemRvAdapterHorizontal()
    var recommendRvAdapter = OwnerItemRvAdapter()
    lateinit var customDialog:CustomProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        customDialog  = CustomProgressDialog(activity!!)

        binding.testBtn3.setOnClickListener {
            searchViewModel.test()
        }

        searchViewModel =
            ViewModelProvider(activity!!, SearchViewModel.Factory(activity!!.application)).get(
                SearchViewModel::class.java
            )

        homeViewModel =
            ViewModelProvider(activity!!, HomeViewModel.Factory(activity!!.application)).get(
                HomeViewModel::class.java
            )

        binding.apply {
            fm = this@SearchFragment
            svm = searchViewModel
        }

        searchViewModel.getHotItemList().observe(this, Observer { itemList ->
            hotitemRvAdapter.setList(itemList)
        })

        searchViewModel.getRecommendItemList().observe(this, Observer { itemList ->
            recommendRvAdapter.setList(itemList)
        })

        homeViewModel.getIsStartItemActivity().observe(this, Observer { isStartActivity ->
            if(isStartActivity==2){
                startItemActivity()
            }
        })



        initHotItemRv()
        initHotItemList()
        initRecommdItemRv()
        initRecommendItemList()
    }


    private fun initRecommendItemList() {
        searchViewModel.setRecommendItemList()
    }

    private fun initRecommdItemRv() {
        binding.fmSearchRvRecommend.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(activity, 2)
            addItemDecoration(GridSpacingItemDecoration(2, 50, false))
            adapter = recommendRvAdapter
        }

        recommendRvAdapter.listener = object : OwnerItemRvAdapter.ClickListener {
            override fun onClick(position: Int) {
                beforeStartItemActivity(position, "fromRecommend")
            }
        }
    }

    private fun initHotItemList() {
        searchViewModel.setHotItemList()
    }

    private fun initHotItemRv() {
        binding.fmSearchRvHotitem.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(HorizonSpacingItemDecoration(50))
            adapter = hotitemRvAdapter
        }

        hotitemRvAdapter.listener = object : OwnerItemRvAdapterHorizontal.ClickListener {
            override fun onClick(position: Int) {
                beforeStartItemActivity(position, "fromHotItem")
            }
        }
    }

    fun beforeStartItemActivity(position: Int, fromRecycler:String) {
        customDialog.show()
        if(fromRecycler == "fromRecommend") {
            homeViewModel.setselectedItem(recommendRvAdapter.itemList[position].id!!)
            homeViewModel.setselectedItemOwner(recommendRvAdapter.itemList[position].userId!!)
        }else if(fromRecycler == "fromHotItem"){
            homeViewModel.setselectedItem(hotitemRvAdapter.itemList[position].id!!)
            homeViewModel.setselectedItemOwner(hotitemRvAdapter.itemList[position].userId!!)
        }
    }

    fun startItemActivity() {
        customDialog.dismiss()
        homeViewModel.clearIsStartItemActivity()
        startActivity(Intent(activity, ItemActivity::class.java))
    }
}
