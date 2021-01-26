package com.mvvm.mycarrot.presentation.my

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.recyclerView.CollectRvAdapter
import com.mvvm.mycarrot.adapter.recyclerView.GridSpacingItemDecoration
import com.mvvm.mycarrot.databinding.ActivityCollectBinding
import com.mvvm.mycarrot.presentation.common.CustomProgressDialog
import com.mvvm.mycarrot.presentation.common.ItemActivity

class CollectActivity : AppCompatActivity() {

    lateinit var binding: ActivityCollectBinding
    lateinit var myViewModel: MyViewModel
    lateinit var customDialog: CustomProgressDialog
    private var collectRvAdapter =
        CollectRvAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        customDialog =
            CustomProgressDialog(this)



        initStatusBar()
        initViewModel()
        initBinding()
        initCollectRv()
        initCollectItemList()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_collect)
        binding.apply {
            lifecycleOwner = this@CollectActivity
            av = this@CollectActivity
        }

    }

    private fun initViewModel() {
        myViewModel = ViewModelProvider(
            this, MyViewModel.Factory(application)
        ).get(MyViewModel::class.java)

        myViewModel.getcollectItemList().observe(this, Observer { inputList ->
            collectRvAdapter.setList(inputList)
        })

        myViewModel.getIsStartItemActivity().observe(this, Observer { isStartActivity ->
            if (isStartActivity == 2 && myViewModel.getselectedFragment() == "collectAv") {
                startItemActivity()
            }
        })
    }

    private fun initCollectRv() {
        binding.collectRv.run {
            setHasFixedSize(true)
            layoutManager = GridLayoutManager(this@CollectActivity, 2)
            addItemDecoration(
                GridSpacingItemDecoration(
                    2,
                    50,
                    false
                )
            )
            adapter = collectRvAdapter
        }

        collectRvAdapter.listener = object : CollectRvAdapter.ClickListener {
            override fun onClick(position: Int) {
                beforeStartItemActivity(position)
            }
        }
    }

    fun beforeStartItemActivity(position: Int) {
        customDialog.show()
        myViewModel.setselectedItem(collectRvAdapter.itemList[position].id!!, "collectAv")
        myViewModel.setselectedItemOwner(collectRvAdapter.itemList[position].userId!!, "collectAv")
    }

    fun startItemActivity() {
        customDialog.dismiss()
        myViewModel.clearIsStartItemActivity()
        startActivity(Intent(this, ItemActivity::class.java))
    }

    fun initCollectItemList() {
        myViewModel.setcollectItemList()
    }

    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }
}
