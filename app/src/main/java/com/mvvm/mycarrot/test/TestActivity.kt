package com.mvvm.mycarrot.test

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.HorizonSpacingItemDecoration
import com.mvvm.mycarrot.adapter.OwnerItemRvAdapterHorizontal
import com.mvvm.mycarrot.view.navigation.SearchTradingFragment
import com.mvvm.mycarrot.view.navigation.SearchUserFragment
import com.mvvm.mycarrot.viewModel.FirebaseViewModel
import com.mvvm.mycarrot.viewModel.HomeViewModel
import com.mvvm.mycarrot.viewModel.TestViewModel
import kotlinx.android.synthetic.main.activity_test.*

class TestActivity : AppCompatActivity() {

    var uri: Uri? = null
    lateinit var viewModel: FirebaseViewModel
    lateinit var homeViewModel: HomeViewModel
    lateinit var testViewModel:TestViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        //********************************** DEFAULT **********************************//


        viewModel = ViewModelProvider(
            this,
            FirebaseViewModel.Factory(application)
        ).get(FirebaseViewModel::class.java)

        homeViewModel = ViewModelProvider(
            this,
            HomeViewModel.Factory(application)
        ).get(HomeViewModel::class.java)

        testViewModel = ViewModelProvider(
            this,
            TestViewModel.Factory(application)
        ).get(TestViewModel::class.java)


        test_btn.setOnClickListener {
            var searchTradingFragment = SearchTradingFragment()
            var searchUserFragment = SearchUserFragment()

        }

        // *************************************************************************** //
    }

    fun test(){
        testViewModel.test()
    }

}
