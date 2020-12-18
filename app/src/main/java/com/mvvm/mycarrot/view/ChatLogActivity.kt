package com.mvvm.mycarrot.view

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.ChatLogFrom
import com.mvvm.mycarrot.adapter.ChatLogTo
import com.mvvm.mycarrot.databinding.ActivityChatLogBinding
import com.mvvm.mycarrot.model.LatestMessageDTO
import com.mvvm.mycarrot.viewModel.ChatLogViewModel
import com.mvvm.mycarrot.viewModel.HomeViewModel
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*

class ChatLogActivity : AppCompatActivity() {

    lateinit var binding: ActivityChatLogBinding
    lateinit var homeViewModel: HomeViewModel
    lateinit var chatLogViewModel:ChatLogViewModel
    lateinit var latestMessageDTO: LatestMessageDTO
    private val mAdapter = GroupAdapter<GroupieViewHolder>()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        homeViewModel = ViewModelProvider(
            this, HomeViewModel.Factory(application)
        ).get(HomeViewModel::class.java)

        chatLogViewModel = ViewModelProvider(
            this, ChatLogViewModel.Factory(application)
        ).get(ChatLogViewModel::class.java)

        latestMessageDTO = initLatest()


        binding = DataBindingUtil.setContentView(this, R.layout.activity_chat_log)
        binding.apply {
            av = this@ChatLogActivity
            lifecycleOwner = this@ChatLogActivity
            latestMessage = latestMessageDTO
            chatLogvm = chatLogViewModel
        }


        initStatusBar()
        initRv()


        binding.chatlogIvAdd.setOnClickListener {
        }
        binding.chatlogIvSend.setOnClickListener {
        }



    }


    fun sendMessage(){
        chatLogViewModel.sendMessage(latestMessageDTO)
    }









    private fun initRv() {
        binding.chatlogRv.adapter = mAdapter
    }


    fun startItemActivity() {
        homeViewModel.clearIsStartItemActivity()
        startActivity(Intent(this, ItemActivity::class.java))
    }

    private fun initLatest(): LatestMessageDTO {
        // CharFragment 에서 넘어왔을시 !=Null
        var latest = intent.getSerializableExtra("LatestMessageDTO") as LatestMessageDTO?

        // ItemActivity 에서 넘어왔을시
        if (latest == null) {
            latest = LatestMessageDTO(
                homeViewModel.getCurrentUserObject().value!!.userId!!,
                homeViewModel.getselectedItemOwner().userId!!,
                homeViewModel.getselectedItem().id!!,
                homeViewModel.getselectedItemOwner().nickname!!,
                homeViewModel.getselectedItemOwner().profileUrl!!,
                homeViewModel.getselectedItemOwner().location!!,
                homeViewModel.getselectedItemOwner().temperature!!,
                homeViewModel.getselectedItem().title.joinToString(" ")!!,
                homeViewModel.getselectedItem().price!!,
                homeViewModel.getselectedItem().imageList[0],
                0L,
                "",
                ""
            )
        } else {
            // Null이 아닐시, charFragmet에서 넘어왔으니 repository의 selecteditem이 설정 안되어있으니 설정해줌
            homeViewModel.setselectedItem(latest.itemUid, "homeFm")
            homeViewModel.setselectedItemOwner(latest.yourUid, "homeFm")
        }

        return latest
    }


    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }
}

class Test(var a :String, var b :String)