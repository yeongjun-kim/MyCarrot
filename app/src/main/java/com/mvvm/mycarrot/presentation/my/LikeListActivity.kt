package com.mvvm.mycarrot.presentation.my

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.recyclerView.ItemRvAdapter
import com.mvvm.mycarrot.databinding.ActivityLikeListBinding
import com.mvvm.mycarrot.presentation.common.CustomProgressDialog
import com.mvvm.mycarrot.presentation.common.ItemActivity

class LikeListActivity : AppCompatActivity() {

    lateinit var binding: ActivityLikeListBinding
    lateinit var myViewModel: MyViewModel
    lateinit var customDialog: CustomProgressDialog
    var itemRvAdapter = ItemRvAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        customDialog =
            CustomProgressDialog(this)

        initViewModel()
        initBinding()
        initRv()
        initStatusBar()

    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_like_list)
        binding.apply {
            av = this@LikeListActivity
            lifecycleOwner = this@LikeListActivity
        }
    }

    private fun initViewModel() {
        myViewModel = ViewModelProvider(
            this, MyViewModel.Factory(application)
        ).get(MyViewModel::class.java)

        myViewModel.getlikeItemList().observe(this, Observer { itemList ->
            itemRvAdapter.setList(itemList)
        })

        myViewModel.getIsStartItemActivity().observe(this, Observer { isStartActivity ->
            if (isStartActivity == 2 && myViewModel.getselectedFragment() == "likeListAv") {
                startItemActivity()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        initLikeListItem()
    }

    private fun initLikeListItem() {
        myViewModel.setlikeItemList()
    }


    private fun initRv() {
        binding.likeListRv.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@LikeListActivity)
            adapter = itemRvAdapter
        }

        itemRvAdapter.listener = object : ItemRvAdapter.ClickListener {
            override fun onClick(position: Int) {
                beforeStartItemActivity(position)
            }

        }
    }

    fun beforeStartItemActivity(position: Int) {
        customDialog.show()
        myViewModel.setselectedItem(itemRvAdapter.itemList[position].id!!, "likeListAv")
        myViewModel.setselectedItemOwner(itemRvAdapter.itemList[position].userId!!, "likeListAv")
    }

    fun startItemActivity() {
        customDialog.dismiss()
        myViewModel.clearIsStartItemActivity()
        startActivity(Intent(this, ItemActivity::class.java))
    }

    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }
}
