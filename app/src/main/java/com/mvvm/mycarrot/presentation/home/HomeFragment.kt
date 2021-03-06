package com.mvvm.mycarrot.presentation.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.recyclerView.ItemRvAdapter
import com.mvvm.mycarrot.databinding.FragmentHomeBinding
import com.mvvm.mycarrot.presentation.common.CustomProgressDialog
import com.mvvm.mycarrot.presentation.common.ItemActivity

class HomeFragment : Fragment() {

    private lateinit var homeViewModel: HomeViewModel
    private lateinit var binding: FragmentHomeBinding
    private lateinit var customDialog: CustomProgressDialog

    val SETUP_TOWN_ACTIVITY = 1234
    var itemRvAdapter = ItemRvAdapter()
    var isFirstCreate = true
    var beforeExtraArrange = 0.0


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        customDialog =
            CustomProgressDialog(activity!!)

        initViewModel()
        initBinding()
        initRv()
        initSwipeListener()
        refreshLastLoginTime()
        checkIsFromCategoryFragment()

    }

    private fun initBinding() {
        binding.apply {
            homeVm = homeViewModel
            fm = this@HomeFragment
            lifecycleOwner = this@HomeFragment
        }
    }

    private fun initViewModel() {

        homeViewModel = ViewModelProvider(
            activity!!, HomeViewModel.Factory(activity!!.application)
        ).get(HomeViewModel::class.java)

        homeViewModel.getHomeItems().observe(this, Observer { itemList ->
            itemRvAdapter.setList(itemList)
        })

        homeViewModel.getIsStartItemActivity().observe(this, Observer { isStartActivity ->
            if (isStartActivity == 2 && homeViewModel.getselectedFragment() == "homeFm") {
                startItemActivity()
            }
        })
    }


    private fun refreshLastLoginTime() {
        homeViewModel.refreshLastLoginTime()
    }

    private fun checkIsFromCategoryFragment() {
        if (homeViewModel.isFromCategoryFragment) {

            //CategoryFm에서 변화가 생긴것
            if (homeViewModel.tempCategoryList.sorted() != homeViewModel.categoryList.sorted()) {
                refreshItem()
                homeViewModel.tempCategoryList = homeViewModel.categoryList.toMutableList()
            }
            homeViewModel.isFromCategoryFragment = false
        }
    }

    private fun initSwipeListener() {
        binding.homeSrl.setOnRefreshListener {
            refreshItem()
            binding.homeSrl.isRefreshing = false
        }
    }

    private fun refreshItem() {
        binding.homeRv.removeAllViewsInLayout()
        homeViewModel.clearHomeItem()
        homeViewModel.clearHomeItemQuery()
        homeViewModel.setHomeItems()
    }

    private fun initRv() {
        binding.homeRv.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = itemRvAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastPostiion =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    val totalCount = recyclerView.adapter!!.itemCount

                    if (lastPostiion == totalCount - 1) {
                        homeViewModel.setHomeItems()
                    }
                }
            })
        }
        itemRvAdapter.listener = object : ItemRvAdapter.ClickListener {
            override fun onClick(position: Int) {
                beforeStartItemActivity(position)
            }

        }
        if (isFirstCreate) {
            homeViewModel.setHomeItems()
            isFirstCreate = false
        }
    }

    fun beforeStartItemActivity(position: Int) {
        customDialog.show()
        homeViewModel.setselectedItem(itemRvAdapter.itemList[position].id!!, "homeFm")
        homeViewModel.setselectedItemOwner(itemRvAdapter.itemList[position].userId!!, "homeFm")
    }

    fun startItemActivity() {
        customDialog.dismiss()
        homeViewModel.clearIsStartItemActivity()
        startActivity(Intent(activity, ItemActivity::class.java))
    }

    fun startSetupTownActivity() {
        beforeExtraArrange = homeViewModel.getExtraArrange()
        startActivityForResult(Intent(activity, SetupTownActivity::class.java), SETUP_TOWN_ACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SETUP_TOWN_ACTIVITY) {
            // extraArrange 가 변경됐다면 itemList update
            if (beforeExtraArrange != homeViewModel.getExtraArrange()) refreshItem()
        }
    }

    fun filterCategory() {
        homeViewModel.tempCategoryList = homeViewModel.categoryList.toMutableList()
        activity!!.supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            .addToBackStack(null)
            .replace(R.id.main_fl,
                FilterCategoryFragment()
            )
            .commit()
    }


}