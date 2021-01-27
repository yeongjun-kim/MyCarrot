package com.mvvm.mycarrot.presentation.search

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.recyclerView.*
import com.mvvm.mycarrot.databinding.FragmentSearchTradingBinding
import com.mvvm.mycarrot.presentation.common.CustomProgressDialog
import com.mvvm.mycarrot.presentation.common.ItemActivity
import com.mvvm.mycarrot.presentation.home.HomeViewModel


class SearchTradingFragment : Fragment() {

    lateinit var binding: FragmentSearchTradingBinding
    lateinit var searchViewModel: SearchViewModel
    lateinit var homeViewModel: HomeViewModel // Item Click 에 대한 로직이 있어서 Click에 사용 (selectedItem)

    var hotitemRvAdapter =
        OwnerItemRvAdapterHorizontal()
    var recommendRvAdapter =
        OwnerItemRvAdapter()
    var searchItemRvAdapter =
        ItemRvAdapter()

    lateinit var customDialog: CustomProgressDialog


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_search_trading, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        customDialog =
            CustomProgressDialog(activity!!)

        initViewModel()
        initBinding()
        initHotItemRv()
        initHotItemList()
        initRecommdItemRv()
        initRecommendItemList()
        initSearchItemRv()

    }

    private fun initBinding() {
        binding.apply {
            fm = this@SearchTradingFragment
            lifecycleOwner = this@SearchTradingFragment
            svm = searchViewModel
        }
    }

    private fun initViewModel() {
        searchViewModel =
            ViewModelProvider(activity!!, SearchViewModel.Factory(activity!!.application)).get(
                SearchViewModel::class.java
            )

        homeViewModel =
            ViewModelProvider(activity!!, HomeViewModel.Factory(activity!!.application)).get(
                HomeViewModel::class.java
            )

        searchViewModel.getHotItemList().observe(this, Observer { itemList ->
            hotitemRvAdapter.setList(itemList)
        })

        searchViewModel.getRecommendItemList().observe(this, Observer { itemList ->
            recommendRvAdapter.setList(itemList)
        })

        searchViewModel.getKeywordItemList().observe(this, Observer { itemList ->
            searchItemRvAdapter.setList(itemList)
        })

        homeViewModel.getIsStartItemActivity().observe(this, Observer { isStartActivity ->
            if (isStartActivity == 2 && homeViewModel.getselectedFragment() == "searchTradingFm") {
                startItemActivity()
            }
        })
    }

    private fun initSearchItemRv() {
        binding.searchTradingRvSearchitem.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = searchItemRvAdapter
        }

        searchItemRvAdapter.listener = object : ItemRvAdapter.ClickListener {
            override fun onClick(position: Int) {
                Log.d("fhrm", "SearchTradingFragment -onClick(),    : click")
                beforeStartItemActivity(position, "fromSearchItem")
            }
        }
    }

    /*
     SearchFragmnet에서 Keyword 입력 후 Enter 시 검색 결과 Rv Show, "toSearchRv"
     [취소] 버튼 클릭 시 일반 NestedScrollView Show, "toNestedScrollView"
     */
    fun changeLayout(mode: String) {
        if (mode == "toSearchRv") {
            binding.searchTradingNsv.visibility = View.INVISIBLE
            binding.searchTradingClSearchitem.visibility = View.VISIBLE
        } else if (mode == "toNestedScrollView") {
            binding.searchTradingNsv.visibility = View.VISIBLE
            binding.searchTradingClSearchitem.visibility = View.INVISIBLE
        }
    }


    private fun initRecommendItemList() {
        searchViewModel.setRecommendItemList()
    }

    private fun initRecommdItemRv() {
        binding.searchTradingRvRecommend.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(activity, 2)
            addItemDecoration(
                GridSpacingItemDecoration(
                    2,
                    50,
                    false
                )
            )
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
        binding.searchTradingRvHotitem.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity, LinearLayoutManager.HORIZONTAL, false)
            addItemDecoration(
                HorizonSpacingItemDecoration(
                    50
                )
            )
            adapter = hotitemRvAdapter
        }

        hotitemRvAdapter.listener = object : OwnerItemRvAdapterHorizontal.ClickListener {
            override fun onClick(position: Int) {
                beforeStartItemActivity(position, "fromHotItem")
            }
        }
    }

    fun beforeStartItemActivity(position: Int, fromRecycler: String) {
        customDialog.show()
        when (fromRecycler) {
            "fromRecommend" -> {
                homeViewModel.setselectedItem(
                    recommendRvAdapter.itemList[position].id!!,
                    "searchTradingFm"
                )
                homeViewModel.setselectedItemOwner(
                    recommendRvAdapter.itemList[position].userId!!,
                    "searchTradingFm"
                )
            }
            "fromHotItem" -> {
                homeViewModel.setselectedItem(
                    hotitemRvAdapter.itemList[position].id!!,
                    "searchTradingFm"
                )
                homeViewModel.setselectedItemOwner(
                    hotitemRvAdapter.itemList[position].userId!!,
                    "searchTradingFm"
                )
            }
            "fromSearchItem" -> {
                homeViewModel.setselectedItem(
                    searchItemRvAdapter.itemList[position].id!!,
                    "searchTradingFm"
                )
                homeViewModel.setselectedItemOwner(
                    searchItemRvAdapter.itemList[position].userId!!,
                    "searchTradingFm"
                )
            }
        }
    }

    fun startItemActivity() {
        customDialog.dismiss()
        homeViewModel.clearIsStartItemActivity()
        startActivity(Intent(activity, ItemActivity::class.java))
    }

    fun onStartCategoryItemActivity(selectedCategory: String) {
        var intent = Intent(activity, CategoryItemActivity::class.java)
        intent.putExtra("category", selectedCategory)
        startActivity(intent)
    }
}
