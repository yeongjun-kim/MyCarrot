package com.mvvm.mycarrot.view.navigation

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.get
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.ItemRvAdapter
import com.mvvm.mycarrot.databinding.ActivityCategoryItemBinding
import com.mvvm.mycarrot.view.CustomProgressDialog
import com.mvvm.mycarrot.view.ItemActivity
import com.mvvm.mycarrot.viewModel.HomeViewModel
import com.mvvm.mycarrot.viewModel.SearchViewModel
import kotlinx.android.synthetic.main.activity_category_item.*

class CategoryItemActivity : AppCompatActivity() {

    lateinit var binding: ActivityCategoryItemBinding
    lateinit var searchViewModel: SearchViewModel
    lateinit var homeViewModel:HomeViewModel
    lateinit var selectedCategory: String
    var isFirstCreate = true
    var categoryItemAdapter = ItemRvAdapter()
    var customDialog = CustomProgressDialog(this)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_category_item)

        selectedCategory = intent.getStringExtra("category")!!


        binding = DataBindingUtil.setContentView(this, R.layout.activity_category_item)

        binding.apply {
            lifecycleOwner = this@CategoryItemActivity
            av = this@CategoryItemActivity
        }

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

        homeViewModel.getIsStartItemActivity().observe(this, Observer { isStartActivity->
            if(isStartActivity ==2){
                Log.d("fhrm", "CategoryItemActivity -onCreate(),    : ")
                startItemActivity()
            }
        })



        initStatusBar()
        initRv()

    }


    private fun initRv() {
        binding.categoryitemRv.run {
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
        homeViewModel.setselectedItem(categoryItemAdapter.itemList[position].id!!)
        homeViewModel.setselectedItemOwner(categoryItemAdapter.itemList[position].userId!!)
    }

    fun startItemActivity() {
        customDialog.dismiss()
        homeViewModel.clearIsStartItemActivity()
        startActivity(Intent(this, ItemActivity::class.java))
    }


    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
        binding.categoryitemTv.text = selectedCategory
    }
}
