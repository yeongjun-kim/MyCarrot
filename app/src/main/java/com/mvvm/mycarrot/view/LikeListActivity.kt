package com.mvvm.mycarrot.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.ItemRvAdapter
import com.mvvm.mycarrot.databinding.ActivityLikeListBinding
import com.mvvm.mycarrot.viewModel.HomeViewModel

class LikeListActivity : AppCompatActivity() {

    lateinit var binding:ActivityLikeListBinding
    lateinit var homeViewModel:HomeViewModel
    lateinit var customDialog:CustomProgressDialog
    var itemRvAdapter = ItemRvAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        customDialog = CustomProgressDialog(this)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_like_list)
        homeViewModel = ViewModelProvider(
            this, HomeViewModel.Factory(application)
        ).get(HomeViewModel::class.java)

        binding.apply {
            av = this@LikeListActivity
            lifecycleOwner = this@LikeListActivity
        }

        homeViewModel.getlikeItemList().observe(this, Observer { itemList ->
            itemRvAdapter.setList(itemList)
        })

        homeViewModel.getIsStartItemActivity().observe(this, Observer { isStartActivity->
            if(isStartActivity ==2 && homeViewModel.getselectedFragment() == "likeListAv"){
                startItemActivity()
            }
        })


        initRv()

        initStatusBar()
    }

    override fun onResume() {
        super.onResume()
        initLikeListItem()
    }

    private fun initLikeListItem() {
        homeViewModel.setlikeItemList()
    }


    private fun initRv() {
        binding.likelistRv.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(this@LikeListActivity)
            adapter = itemRvAdapter
        }

        itemRvAdapter.listener = object: ItemRvAdapter.ClickListener{
            override fun onClick(position: Int) {
                beforeStartItemActivity(position)
            }

        }
    }

    fun beforeStartItemActivity(position:Int){
        customDialog.show()
        homeViewModel.setselectedItem(itemRvAdapter.itemList[position].id!!, "likeListAv")
        homeViewModel.setselectedItemOwner(itemRvAdapter.itemList[position].userId!!, "likeListAv")
    }

    fun startItemActivity(){
        customDialog.dismiss()
        homeViewModel.clearIsStartItemActivity()
        startActivity(Intent(this,ItemActivity::class.java))
    }

    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }
}
