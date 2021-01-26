package com.mvvm.mycarrot.presentation.my

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.recyclerView.ItemRvAdapter
import com.mvvm.mycarrot.databinding.ActivityBuyListBinding
import com.mvvm.mycarrot.presentation.common.CustomProgressDialog
import com.mvvm.mycarrot.presentation.common.ItemActivity

class BuyListActivity : AppCompatActivity() {

    lateinit var binding: ActivityBuyListBinding
    lateinit var myViewModel: MyViewModel
    lateinit var customDialog: CustomProgressDialog
    var itemRvAdapter = ItemRvAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_list)

        customDialog =
            CustomProgressDialog(this)

        initViewModel()
        initBinding()
        initRv()
        initStatusBar()

    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_buy_list)
        binding.apply {
            lifecycleOwner = this@BuyListActivity
            av = this@BuyListActivity
        }
    }

    private fun initViewModel() {
        myViewModel =
            ViewModelProvider(this, MyViewModel.Factory(application)).get(
                MyViewModel::class.java)


        myViewModel.getbuyItemList().observe(this, Observer { itemList ->
            itemRvAdapter.setList(itemList)
        })

        myViewModel.getIsStartItemActivity().observe(this, Observer { isStartActivity ->
            if (isStartActivity == 2 && myViewModel.getselectedFragment() == "buyListAv") {
                startItemActivity()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        initBuyListItem()
    }

    private fun initBuyListItem() {
        myViewModel.setbuyItemList()
    }

    private fun initRv() {
        binding.buyListRv.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@BuyListActivity)
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
        myViewModel.setselectedItem(itemRvAdapter.itemList[position].id!!, "buyListAv")
        myViewModel.setselectedItemOwner(itemRvAdapter.itemList[position].userId!!, "buyListAv")
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
