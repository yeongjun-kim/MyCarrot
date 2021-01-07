package com.mvvm.mycarrot.view.buyComplete

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.viewModel.BuyCompleteViewModel

class BuyCompleteActivity : AppCompatActivity() {

    lateinit var buyCompleteViewModel: BuyCompleteViewModel
    var selectBuyerFragment = SelectBuyerFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_buy_complete)


        buyCompleteViewModel =ViewModelProvider(this, BuyCompleteViewModel.Factory(application)).get(BuyCompleteViewModel::class.java)


        initItem()
        initChatUserList()
        initStatusBar()
        initSelectBuyerFragment()
    }

    private fun initItem() {
        val itemId = intent.getStringExtra("itemId")!!
        buyCompleteViewModel.setbuyCompleteItem(itemId)
    }

    private fun initChatUserList(){
        val itemId = intent.getStringExtra("itemId")!!
        buyCompleteViewModel.setbuyCompleteChatList(itemId)
    }


    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }

    private fun initSelectBuyerFragment() {

        supportFragmentManager.beginTransaction()
            .setCustomAnimations(
                android.R.animator.fade_in,
                android.R.animator.fade_out,
                android.R.animator.fade_in,
                android.R.animator.fade_out
            )
            .add(R.id.buyComplete_fl, selectBuyerFragment)
            .addToBackStack(null)
            .commit()
    }
}
