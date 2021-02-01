package com.mvvm.mycarrot.presentation.my.profile

import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.recyclerView.ItemRvMannerDetailAdapter
import com.mvvm.mycarrot.databinding.ActivityMannerDetailBinding
import com.mvvm.mycarrot.data.model.ReviewObject
import com.mvvm.mycarrot.presentation.home.HomeViewModel

class MannerDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMannerDetailBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
        initBinding()
        initPositiveRv()
        initNegativeRv()
        initStatusBar()

    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_manner_detail)
        binding.apply {
            lifecycleOwner = this@MannerDetailActivity
            av = this@MannerDetailActivity
        }
    }

    private fun initViewModel() {
        homeViewModel = ViewModelProvider(
            this,
            HomeViewModel.Factory(application)
        ).get(HomeViewModel::class.java)
    }

    private fun initNegativeRv() {
        var targetId = homeViewModel.getselectedItemOwner().userId
        var myId = homeViewModel.getCurrentUserObject().value!!.userId

        // 타인의 Profile
        if (targetId != myId) {
            binding.mannerDetailCl3.visibility = View.GONE
        } else {

            var mannerList = mutableListOf(
                ReviewObject(
                    homeViewModel.getselectedItemOwner().negative_1.toString(),
                    getString(R.string.negative_1)
                ),
                ReviewObject(
                    homeViewModel.getselectedItemOwner().negative_2.toString(),
                    getString(R.string.negative_2)
                ),
                ReviewObject(
                    homeViewModel.getselectedItemOwner().negative_3.toString(),
                    getString(R.string.negative_3)
                ),
                ReviewObject(
                    homeViewModel.getselectedItemOwner().negative_4.toString(),
                    getString(R.string.negative_4)
                )
            )


            var mAdapter =
                ItemRvMannerDetailAdapter()
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


        var mAdapter =
            ItemRvMannerDetailAdapter()
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
