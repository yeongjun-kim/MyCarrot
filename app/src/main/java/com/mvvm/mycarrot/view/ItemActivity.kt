package com.mvvm.mycarrot.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import androidx.core.widget.NestedScrollView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.like.LikeButton
import com.like.OnLikeListener
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.GridSpacingItemDecoration
import com.mvvm.mycarrot.adapter.ItemVpAdapter
import com.mvvm.mycarrot.adapter.OwnerItemRvAdapter
import com.mvvm.mycarrot.databinding.ActivityItemBinding
import com.mvvm.mycarrot.viewModel.HomeViewModel
import kotlinx.android.synthetic.main.activity_item.*

class ItemActivity : AppCompatActivity() {

    lateinit var homeViewModel: HomeViewModel
    lateinit var binding: ActivityItemBinding
    var ownerItemRvAdapter = OwnerItemRvAdapter()
    var recommendItemRvAdapter = OwnerItemRvAdapter()
    var customDialog = CustomProgressDialog(this)
    private var vpAdapter = ItemVpAdapter(arrayListOf())
    var flag = false


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

        initStatusBar()
        initLikeButtonListener() // LikeButton은 Databinding 이 안되기때문에 clickListener 해야함
        initRvOwnerItem()
        initRvRecommendItem()
        initVp()
        initOtherItemList()

        item_nsv.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
            if (scrollY < 900) changeTopLayoutColor("toTransparent")
            else changeTopLayoutColor("toWhite")
        }





        homeViewModel.getselectedItemOwnersItem().observe(this, Observer { itemList ->
            ownerItemRvAdapter.setList(itemList, 4)
        })

        homeViewModel.getselectedItemRecommendItem().observe(this, Observer { itemList ->
            recommendItemRvAdapter.setList(itemList, null)
        })

        homeViewModel.getIsStartItemActivity().observe(this, Observer { isStartActivity ->
            if (isStartActivity == 2 && homeViewModel.getselectedFragment() == "itemAv") {
                startItemActivity()
            }
        })


        item_test.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
        }
    }

    private fun changeTopLayoutColor(input: String) {
        if (input == "toTransparent") {
            binding.itemClTop.setBackgroundColor(Color.parseColor("#00000000"))
            binding.itemIvBackpress.setImageResource(R.drawable.ic_arrow_back_white_14dp)
            binding.itemTemp10.visibility = View.GONE
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        } else {
            binding.itemClTop.setBackgroundColor(Color.parseColor("#FFFFFF"))
            binding.itemIvBackpress.setImageResource(R.drawable.ic_arrow_back_black_14dp)
            binding.itemTemp10.visibility = View.VISIBLE
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        }
    }


    private fun initOtherItemList() {
        homeViewModel.setSelectedItemOwnersItem()
        homeViewModel.setSelectedItemRecommendItem()
    }

    private fun initVp() {
        binding.itemVp.adapter = vpAdapter
        binding.itemVp.orientation = ViewPager2.ORIENTATION_HORIZONTAL
        vpAdapter.setList(homeViewModel.getselectedItem().imageList)
        binding.itemVp.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
            }
        })

        binding.itemCi.setViewPager(binding.itemVp) // ViewPager White Dot Indicator
    }

    private fun initRvRecommendItem() {
        binding.itemRvRecommenditem.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@ItemActivity, 2)
            addItemDecoration(GridSpacingItemDecoration(2, 50, false))
            adapter = recommendItemRvAdapter
        }

        recommendItemRvAdapter.listener = object : OwnerItemRvAdapter.ClickListener {
            override fun onClick(position: Int) {
                beforeStartItemActivity(position, recommendItemRvAdapter)
            }
        }
    }

    private fun initRvOwnerItem() {
        binding.itemRvOwnerItem.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@ItemActivity, 2)
            addItemDecoration(GridSpacingItemDecoration(2, 50, false))
            adapter = ownerItemRvAdapter
        }

        ownerItemRvAdapter.listener = object : OwnerItemRvAdapter.ClickListener {
            override fun onClick(position: Int) {
                beforeStartItemActivity(position, ownerItemRvAdapter)
            }

        }
    }

    fun beforeStartItemActivity(position: Int, adapter: OwnerItemRvAdapter) {
        customDialog.show()
        homeViewModel.setselectedItem(adapter.itemList[position].id!!,"itemAv")
        homeViewModel.setselectedItemOwner(adapter.itemList[position].userId!!,"itemAv")
    }


    fun startItemActivity() {
        customDialog.dismiss()
        homeViewModel.clearIsStartItemActivity()
        startActivity(Intent(this, ItemActivity::class.java))
    }

    fun startActivitySeeMore() {
        homeViewModel.saveSelectedItemOwnersItem()
        startActivity(Intent(this, SeeMoreActivity::class.java))
    }

    fun startActivityProfile(){
        startActivity(Intent(this, ProfileActivity::class.java))
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

    private fun initStatusBar() {
        window.setFlags(
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
            WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
        )
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_VISIBLE
        window.statusBarColor = Color.TRANSPARENT
    }

}