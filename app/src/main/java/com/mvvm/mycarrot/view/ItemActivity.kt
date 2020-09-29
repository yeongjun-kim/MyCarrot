package com.mvvm.mycarrot.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.like.LikeButton
import com.like.OnLikeListener
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ActivityItemBinding
import com.mvvm.mycarrot.model.ItemObject
import com.mvvm.mycarrot.model.UserObject
import com.mvvm.mycarrot.viewModel.HomeViewModel
import kotlinx.android.synthetic.main.activity_item.*

class ItemActivity : AppCompatActivity() {

    lateinit var homeViewModel: HomeViewModel
    lateinit var binding: ActivityItemBinding

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

        homeViewModel.getIsLiked().observe(this, Observer { isLiked ->
            item_lb.isLiked = isLiked
        })

        homeViewModel.getselectedItem().observe(this, Observer {
            homeViewModel.checkIsLiked()
        })

        homeViewModel.getselectedItemOwner().observe(this, Observer {
            homeViewModel.checkIsLiked()
        })

        initItemAndOwner()
        initLikeButtonListener() // LikeButton은 Databinding 이 안되기때문에 clickListener 해야함


        item_test.setOnClickListener {
        }
    }




    private fun initLikeButtonListener() {
        homeViewModel.checkIsLiked()

        item_lb.setOnLikeListener(object : OnLikeListener {
            override fun liked(likeButton: LikeButton?) {
                homeViewModel.addToLikeList(homeViewModel.getselectedItem().value!!.id!!)
            }

            override fun unLiked(likeButton: LikeButton?) {
                homeViewModel.deleteFromLikeList(homeViewModel.getselectedItem().value!!.id!!)
            }
        })
    }

    fun <T, K, R> LiveData<T>.combineWith(
        liveData: LiveData<K>,
        block: (T?, K?) -> R
    ): LiveData<R> {
        val result = MediatorLiveData<R>()
        result.addSource(this) {
            result.value = block(this.value, liveData.value)
        }
        result.addSource(liveData) {
            result.value = block(this.value, liveData.value)
        }
        return result
    }

    private fun initItemAndOwner() {
        homeViewModel.selectedItem(intent.getStringExtra("itemId"))
        homeViewModel.selectedItemOwner(intent.getStringExtra("ownerId"))
    }
}
