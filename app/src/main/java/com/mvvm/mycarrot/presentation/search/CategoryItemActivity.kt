package com.mvvm.mycarrot.presentation.search

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.recyclerView.ItemRvAdapter
import com.mvvm.mycarrot.databinding.ActivityCategoryItemBinding
import com.mvvm.mycarrot.presentation.common.CustomProgressDialog
import com.mvvm.mycarrot.presentation.common.ItemActivity
import com.mvvm.mycarrot.presentation.home.HomeViewModel

class CategoryItemActivity : AppCompatActivity() {

    private lateinit var binding: ActivityCategoryItemBinding
    private lateinit var searchViewModel: SearchViewModel
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var selectedCategory: String
    private var isFirstCreate = true
    private var categoryItemAdapter = ItemRvAdapter()
    private var customDialog = CustomProgressDialog(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_item)

        selectedCategory = intent.getStringExtra("category")!!

        initViewModel()
        initBinding()
        initStatusBar()
        initRv()

    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_category_item)

        binding.apply {
            lifecycleOwner = this@CategoryItemActivity
            av = this@CategoryItemActivity
        }
    }

    private fun initViewModel() {
        searchViewModel = ViewModelProvider(
            this,
            SearchViewModel.Factory(application)
        ).get(SearchViewModel::class.java)

        homeViewModel = ViewModelProvider(
            this,
            HomeViewModel.Factory(application)
        ).get(HomeViewModel::class.java)

        searchViewModel.getCategoryItemList().observe(this, Observer { itemList ->
            categoryItemAdapter.setList(itemList)
        })

        homeViewModel.getIsStartItemActivity().observe(this, Observer { isStartActivity ->
            if (isStartActivity == 2 && homeViewModel.getselectedFragment() == "categoryItemAv") {
                startItemActivity()
            }
        })
    }


    private fun initRv() {
        binding.categoryItemRv.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@CategoryItemActivity)
            adapter = categoryItemAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastPostiion =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    val totalCount = recyclerView.adapter!!.itemCount

                    if (lastPostiion == totalCount - 1) {
                        searchViewModel.setCategoryItemList(selectedCategory)
                    }
                }
            })
        }
        categoryItemAdapter.listener = object : ItemRvAdapter.ClickListener {
            override fun onClick(position: Int) {
                beforeStartItemActivity(position)
            }

        }
        if (isFirstCreate) {
            searchViewModel.setCategoryItemList(selectedCategory)
            isFirstCreate = false
        }
    }


    fun beforeStartItemActivity(position: Int) {
        customDialog.show()
        homeViewModel.setselectedItem(categoryItemAdapter.itemList[position].id!!, "categoryItemAv")
        homeViewModel.setselectedItemOwner(
            categoryItemAdapter.itemList[position].userId!!,
            "categoryItemAv"
        )
    }

    fun startItemActivity() {
        customDialog.dismiss()
        homeViewModel.clearIsStartItemActivity()
        startActivity(Intent(this, ItemActivity::class.java))
    }


    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
        binding.categoryItemTv.text = selectedCategory
    }
}
