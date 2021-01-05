package com.mvvm.mycarrot.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.CollectRvAdapter
import com.mvvm.mycarrot.adapter.GridSpacingItemDecoration
import com.mvvm.mycarrot.databinding.ActivityCollectBinding
import com.mvvm.mycarrot.model.ItemObject
import com.mvvm.mycarrot.model.UserObject
import com.mvvm.mycarrot.viewModel.HomeViewModel

class CollectActivity : AppCompatActivity() {

    lateinit var binding: ActivityCollectBinding
    lateinit var homeViewModel: HomeViewModel
    var collectRvAdapter = CollectRvAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_collect)

        homeViewModel = ViewModelProvider(
            this, HomeViewModel.Factory(application)
        ).get(HomeViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@CollectActivity
            av = this@CollectActivity
        }

        initStatusBar()

        homeViewModel.getcollectItemList().observe(this, Observer { inputList ->
            collectRvAdapter.setList(inputList)
        })



        initCollectRv()
        initCollectItemList()
    }

    private fun initCollectRv() {
        binding.collectRv.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@CollectActivity, 2)
            addItemDecoration(GridSpacingItemDecoration(2, 50, false))
            adapter = collectRvAdapter
        }

        collectRvAdapter.listener = object:CollectRvAdapter.ClickListener{
            override fun onClick(position: Int) {
            }
        }
    }

    fun initCollectItemList(){
        homeViewModel.setcollectItemList()
    }
    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }
}
