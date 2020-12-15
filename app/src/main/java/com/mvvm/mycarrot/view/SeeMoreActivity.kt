package com.mvvm.mycarrot.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.tabs.TabLayout
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.ItemRvAdapter
import com.mvvm.mycarrot.databinding.ActivitySeeMoreBinding
import com.mvvm.mycarrot.viewModel.HomeViewModel
import com.mvvm.mycarrot.viewModel.SeeMoreViewModel

class SeeMoreActivity : AppCompatActivity() {

    lateinit var binding:ActivitySeeMoreBinding
    lateinit var seeMoreViewModel: SeeMoreViewModel
    lateinit var homeViewModel: HomeViewModel
    lateinit var customDialog: CustomProgressDialog
    var itemRvAdapter = ItemRvAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)


        binding = DataBindingUtil.setContentView(this, R.layout.activity_see_more)
        customDialog = CustomProgressDialog(this)
        seeMoreViewModel = ViewModelProvider(
            this, SeeMoreViewModel.Factory(this.application)
        ).get(SeeMoreViewModel::class.java)

        homeViewModel = ViewModelProvider(
            this, HomeViewModel.Factory(this.application)
        ).get(HomeViewModel::class.java)

        homeViewModel.getIsStartItemActivity().observe(this, Observer { isStartActivity->
            if(isStartActivity ==2 && homeViewModel.getselectedFragment() == "seeMoreAv"){
                startItemActivity()
            }
        })

        binding.seemoreTl.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
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

        initRv()
        initStatusBar()


        binding.testBtn2.setOnClickListener {
        }

    }

    private fun initRv() {
        itemRvAdapter.setList(seeMoreViewModel.itemList)

        binding.seemoreRv.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@SeeMoreActivity)
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
        startActivity(Intent(this, ItemActivity::class.java))
    }

    fun beforeStartItemActivity(position:Int){
        customDialog.show()
        homeViewModel.setselectedItem(itemRvAdapter.itemList[position].id!!, "seeMoreAv")
        homeViewModel.setselectedItemOwner(itemRvAdapter.itemList[position].userId!!,"seeMoreAv")
    }

    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }

}

