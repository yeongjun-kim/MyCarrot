package com.mvvm.mycarrot.view.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide.init

import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.ItemRvAdapter
import com.mvvm.mycarrot.databinding.FragmentHomeBinding
import com.mvvm.mycarrot.viewModel.FirebaseViewModel
import com.mvvm.mycarrot.viewModel.HomeViewModel
import com.sucho.placepicker.Constants
import com.sucho.placepicker.MapType
import com.sucho.placepicker.PlacePicker
import kotlinx.android.synthetic.main.fragment_home.*

class HomeFragment : Fragment() {

    lateinit var homeViewModel: HomeViewModel
    lateinit var binding: FragmentHomeBinding
    var itemRvAdapter = ItemRvAdapter()

    init {
        Log.d("fhrm", "HomeFragment -(),    : here")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        Log.d("fhrm", "HomeFragment -onCreateView(),    : ")
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        Log.d("fhrm", "HomeFragment -onActivityCreated(),    : ")
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


        btn_test.setOnClickListener {
        }

        initRv()
        initSwipeListener()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Log.d("fhrm", "HomeFragment -onAttach(),    : ")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d("fhrm", "HomeFragment -onCreate(),    : ")
    }

    override fun onStart() {
        super.onStart()
        Log.d("fhrm", "HomeFragment -onStart(),    : ")
    }

    override fun onResume() {
        super.onResume()
        Log.d("fhrm", "HomeFragment -onResume(),    : ")
    }

    override fun onPause() {
        super.onPause()
        Log.d("fhrm", "HomeFragment -onPause(),    : ")
    }

    override fun onStop() {
        super.onStop()
        Log.d("fhrm", "HomeFragment -onStop(),    : ")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        Log.d("fhrm", "HomeFragment -onDestroyView(),    : ")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("fhrm", "HomeFragment -onDestroy(),    : ")
    }

    override fun onDetach() {
        super.onDetach()
        Log.d("fhrm", "HomeFragment -onDetach(),    : ")
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
        homeViewModel.setHomeItems()
    }

    fun startSetupTownActivity() {
        startActivity(Intent(activity, SetupTownActivity::class.java))
    }

    fun filterCategory() {
        activity!!.supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.main_fl, CategoryFragment())
            .commit()
    }


}


//아이템 겟
//
//이 조건으로 firebaseStore get 하는거 코드 만들것
//
//
//query 조건
//1. lat+extraArrange, long+extraArrange
//1. wherein category
//1. orderby timestamp
//1. 닷limit()
//1. get은 paging

