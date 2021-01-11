package com.mvvm.mycarrot.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.ItemRvMannerAdapter
import com.mvvm.mycarrot.adapter.ItemRvMannerDetailAdapter
import com.mvvm.mycarrot.databinding.ActivityMannerDetailBinding
import com.mvvm.mycarrot.model.ReviewObject
import com.mvvm.mycarrot.viewModel.HomeViewModel
import kotlinx.android.synthetic.main.activity_manner_detail.*

class MannerDetailActivity : AppCompatActivity() {

    lateinit var binding: ActivityMannerDetailBinding
    lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_manner_detail)
        homeViewModel = ViewModelProvider(
            this,
            HomeViewModel.Factory(application)
        ).get(HomeViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@MannerDetailActivity
            av = this@MannerDetailActivity
        }


        initPositiveRv()
        initNegativeRv()
        initStatusBar()


    }

    private fun initNegativeRv() {
        var targetId = homeViewModel.getselectedItemOwner().userId
        var myId = homeViewModel.getCurrentUserObject().value!!.userId

        // 타인의 Profile
        if (targetId != myId) {
            binding.mannerDetailCl3.visibility = View.GONE
        } else {

            var mannerList = mutableListOf(
                ReviewObject(homeViewModel.getselectedItemOwner().negative_1.toString(),getString(R.string.negative_1)),
                ReviewObject(homeViewModel.getselectedItemOwner().negative_2.toString(),getString(R.string.negative_2)),
                ReviewObject(homeViewModel.getselectedItemOwner().negative_3.toString(),getString(R.string.negative_3)),
                ReviewObject(homeViewModel.getselectedItemOwner().negative_4.toString(),getString(R.string.negative_4))
            )


            var mAdapter = ItemRvMannerDetailAdapter()
            mAdapter.setList(mannerList.filter { it.count != "0" })

            binding.mannerDetailRvNegative.run {
                setHasFixedSize(true)
                layoutManager = LinearLayoutManager(context)
                adapter = mAdapter
            }
        }

    }

    private fun initPositiveRv() {
        var mannerList = mutableListOf(
            ReviewObject(
                homeViewModel.getselectedItemOwner().positive_1.toString(),
                getString(R.string.positive_1)
            ),
            ReviewObject(
                homeViewModel.getselectedItemOwner().positive_2.toString(),
                getString(R.string.positive_2)
            ),
            ReviewObject(
                homeViewModel.getselectedItemOwner().positive_3.toString(),
                getString(R.string.positive_3)
            ),
            ReviewObject(
                homeViewModel.getselectedItemOwner().positive_4.toString(),
                getString(R.string.positive_4)
            )
        )


        var mAdapter = ItemRvMannerDetailAdapter()
        mAdapter.setList(mannerList.filter { it.count != "0" })

        binding.mannerDetailRvPositive.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(context)
            adapter = mAdapter
        }
    }

    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }
}
