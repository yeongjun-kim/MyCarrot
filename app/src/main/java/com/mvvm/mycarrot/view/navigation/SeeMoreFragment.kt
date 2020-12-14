package com.mvvm.mycarrot.view.navigation

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.ItemRvAdapter
import com.mvvm.mycarrot.databinding.FragmentSeemoreBinding
import com.mvvm.mycarrot.view.CustomProgressDialog
import com.mvvm.mycarrot.view.ItemActivity
import com.mvvm.mycarrot.viewModel.HomeViewModel
import com.mvvm.mycarrot.viewModel.SeeMoreViewModel

class SeeMoreFragment : Fragment() {

    lateinit var binding: FragmentSeemoreBinding
    lateinit var seeMoreViewModel: SeeMoreViewModel
    lateinit var homeViewModel: HomeViewModel
    lateinit var customDialog: CustomProgressDialog
    var itemRvAdapter = ItemRvAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_seemore, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        customDialog = CustomProgressDialog(activity!!)

        seeMoreViewModel = ViewModelProvider(
            activity!!, SeeMoreViewModel.Factory(activity!!.application)
        ).get(SeeMoreViewModel::class.java)

        homeViewModel = ViewModelProvider(
            activity!!, HomeViewModel.Factory(activity!!.application)
        ).get(HomeViewModel::class.java)

        homeViewModel.getIsStartItemActivity().observe(this, Observer { isStartActivity->
            if(isStartActivity ==2 && homeViewModel.getselectedFragment() == "seeMoreFm"){
                startItemActivity()
            }
        })

        initRv()



        binding.fmSeemoreTl.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(tab: TabLayout.Tab?) {}
            override fun onTabUnselected(tab: TabLayout.Tab?) {}
            override fun onTabSelected(tab: TabLayout.Tab) {
                when (tab.position) {
                    0 -> Log.d("fhrm", "SeeMoreFragment -onTabSelected(),    0 selected") // TODO(모든 상품 전체 나열, 완료된건 alpha 파라미터 이용하여 투명도 주기)
                    1 -> Log.d("fhrm", "SeeMoreFragment -onTabSelected(),    1 selected") // TODO(거래중인 아이템 list 구해서 itemRvAdapter.setList(itemList) 할것)
                    2 -> Log.d("fhrm", "SeeMoreFragment -onTabSelected(),    2 selected") // TODO(거래완료 아이템 list 구해서 itemRvAdapter.setList(itemList) 할것)
                }
            }

        })


        binding.testBtn2.setOnClickListener {
        }
    }

    private fun initRv() {
        itemRvAdapter.setList(seeMoreViewModel.itemList)

        binding.fmSeemoreRv.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = itemRvAdapter
        }

        itemRvAdapter.listener = object :ItemRvAdapter.ClickListener{
            override fun onClick(position: Int) {
                beforeStartItemActivity(position)
            }
        }

    }

    fun startItemActivity(){
        customDialog.dismiss()
        homeViewModel.clearIsStartItemActivity()
        startActivity(Intent(activity, ItemActivity::class.java))
    }

    fun beforeStartItemActivity(position:Int){
        customDialog.show()
        homeViewModel.setselectedItem(itemRvAdapter.itemList[position].id!!, "seeMoreFm")
        homeViewModel.setselectedItemOwner(itemRvAdapter.itemList[position].userId!!,"seeMoreFm")
    }

}

