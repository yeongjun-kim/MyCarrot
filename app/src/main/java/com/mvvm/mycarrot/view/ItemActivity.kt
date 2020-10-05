package com.mvvm.mycarrot.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.like.LikeButton
import com.like.OnLikeListener
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.OwnerItemRvAdapter
import com.mvvm.mycarrot.databinding.ActivityItemBinding
import com.mvvm.mycarrot.model.ItemObject
import com.mvvm.mycarrot.model.UserObject
import com.mvvm.mycarrot.viewModel.HomeViewModel
import kotlinx.android.synthetic.main.activity_item.*

class ItemActivity : AppCompatActivity() {

    lateinit var homeViewModel: HomeViewModel
    lateinit var binding: ActivityItemBinding
    var ownerItemRvAdapter = OwnerItemRvAdapter()
    var recommendItemRvAdapter = OwnerItemRvAdapter()
    var customDialog = CustomProgressDialog(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_item)

        homeViewModel = ViewModelProvider(
            this, HomeViewModel.Factory(application)
        ).get(HomeViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_item)
        binding.apply {
            vm = homeViewModel
            av = this@ItemActivity
            lifecycleOwner = this@ItemActivity
        }


        initLikeButtonListener() // LikeButton은 Databinding 이 안되기때문에 clickListener 해야함
        initRvOwnerItem()
        initRvRecommendItem()

        homeViewModel.setSelectedItemOwnersItem()
        homeViewModel.setSelectedItemRecommendItem()


        homeViewModel.getselectedItemOwnersItem().observe(this, Observer { itemList ->
            ownerItemRvAdapter.setList(itemList, 4)
        })

        homeViewModel.getselectedItemRecommendItem().observe(this, Observer { itemList->
            recommendItemRvAdapter.setList(itemList,null)
        })

        homeViewModel.getIsStartItemActivity().observe(this, Observer { isStartActivity ->
            if (isStartActivity == 2) {
                startItemActivity()
            }
        })


        item_test.setOnClickListener {
            homeViewModel.test()
        }
    }

    private fun initRvRecommendItem() {
        binding.itemRvRecommenditem.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@ItemActivity, 2)
            adapter = recommendItemRvAdapter
        }

        recommendItemRvAdapter.listener= object : OwnerItemRvAdapter.ClickListener {
            override fun onClick(position: Int) {
                beforeStartItemActivity(position)
            }
        }
    }

    private fun initRvOwnerItem() {
        binding.itemRvOwnerItem.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@ItemActivity, 2)
            adapter = ownerItemRvAdapter
        }

        ownerItemRvAdapter.listener = object : OwnerItemRvAdapter.ClickListener {
            override fun onClick(position: Int) {
                beforeStartItemActivity(position)
            }

        }
    }

    fun beforeStartItemActivity(position: Int) {
        customDialog.show()
        homeViewModel.setselectedItem(ownerItemRvAdapter.itemList[position].id!!)
        homeViewModel.setselectedItemOwner(ownerItemRvAdapter.itemList[position].userId!!)
    }

    fun startItemActivity() {
        customDialog.dismiss()
        homeViewModel.clearIsStartItemActivity()
        startActivity(Intent(this, ItemActivity::class.java))
    }

    fun startActivitySeeMore() {
        Toast.makeText(this, "더보기", Toast.LENGTH_SHORT).show()
    }


    private fun initLikeButtonListener() {

        item_lb.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton?) {
                homeViewModel.addToLikeList(homeViewModel.getselectedItem().id!!)
            }

            override fun unLiked(likeButton: LikeButton?) {
                homeViewModel.deleteFromLikeList(homeViewModel.getselectedItem().id!!)
            }
        })
    }

}