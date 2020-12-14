package com.mvvm.mycarrot.view.navigation

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.ItemRvAdapter
import com.mvvm.mycarrot.databinding.FragmentHomeBinding
import com.mvvm.mycarrot.view.CustomProgressDialog
import com.mvvm.mycarrot.view.ItemActivity
import com.mvvm.mycarrot.viewModel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    lateinit var homeViewModel: HomeViewModel
    lateinit var binding: FragmentHomeBinding
    lateinit var customDialog:CustomProgressDialog

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

        customDialog = CustomProgressDialog(activity!!)

        homeViewModel = ViewModelProvider(
            activity!!, HomeViewModel.Factory(activity!!.application)
        ).get(HomeViewModel::class.java)

        binding.apply {
            homeVm = homeViewModel
            fm = this@HomeFragment
            lifecycleOwner = this@HomeFragment
        }

        homeViewModel.getHomeItems().observe(this, Observer { itemList ->
            itemRvAdapter.setList(itemList)
        })

        homeViewModel.getIsStartItemActivity().observe(this, Observer { isStartActivity->
            if(isStartActivity ==2 && homeViewModel.getselectedFragment() == "homeFm"){
                startItemActivity()
            }
        })



        btn_test.setOnClickListener {
            refreshItem()
        }

        initRv()
        initSwipeListener()
        checkIsFromCategoryFragment()
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
        binding.fmHomeSrl.setOnRefreshListener {
            refreshItem()
            binding.fmHomeSrl.isRefreshing = false
        }
    }

    private fun refreshItem() {
        binding.fmHomeRv.removeAllViewsInLayout()
        homeViewModel.clearHomeItem()
        homeViewModel.clearHomeItemQuery()
        homeViewModel.setHomeItems()
    }

    private fun initRv() {
        binding.fmHomeRv.run {
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
        itemRvAdapter.listener = object: ItemRvAdapter.ClickListener{
            override fun onClick(position: Int) {
                beforeStartItemActivity(position)
            }

        }
        if(isFirstCreate){
            homeViewModel.setHomeItems()
            isFirstCreate = false
        }
    }
    fun beforeStartItemActivity(position:Int){
        customDialog.show()
        homeViewModel.setselectedItem(itemRvAdapter.itemList[position].id!!, "homeFm")
        homeViewModel.setselectedItemOwner(itemRvAdapter.itemList[position].userId!!, "homeFm")
    }

    fun startItemActivity(){
        customDialog.dismiss()
        homeViewModel.clearIsStartItemActivity()
        startActivity(Intent(activity,ItemActivity::class.java))
    }

    fun startSetupTownActivity() {
        beforeExtraArrange = homeViewModel.getExtraArrange()
        startActivityForResult(Intent(activity, SetupTownActivity::class.java),SETUP_TOWN_ACTIVITY)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SETUP_TOWN_ACTIVITY) {
            // extraArrange 가 변경됐다면 itemList update
            if(beforeExtraArrange != homeViewModel.getExtraArrange()) refreshItem()
        }
    }

    fun filterCategory() {
        homeViewModel.tempCategoryList = homeViewModel.categoryList.toMutableList()
        activity!!.supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .addToBackStack(null)
            .replace(R.id.main_fl, FilterCategoryFragment())
            .commit()
    }


}
