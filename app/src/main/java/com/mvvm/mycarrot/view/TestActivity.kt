package com.mvvm.mycarrot.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.HorizonSpacingItemDecoration
import com.mvvm.mycarrot.adapter.ItemRvAdapter
import com.mvvm.mycarrot.adapter.OwnerItemRvAdapter
import com.mvvm.mycarrot.adapter.OwnerItemRvAdapterHorizontal
import com.mvvm.mycarrot.model.ItemObject
import com.mvvm.mycarrot.viewModel.FirebaseViewModel
import com.mvvm.mycarrot.viewModel.HomeViewModel
import com.sucho.placepicker.AddressData
import com.sucho.placepicker.Constants
import com.sucho.placepicker.MapType
import com.sucho.placepicker.PlacePicker
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_test.*
import kotlinx.android.synthetic.main.activity_write.*
import java.util.*

class TestActivity : AppCompatActivity() {

    var uri: Uri? = null
    lateinit var viewModel: FirebaseViewModel
    lateinit var homeViewModel: HomeViewModel
    var mAdapter = OwnerItemRvAdapterHorizontal()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        viewModel = ViewModelProvider(
            this,
            FirebaseViewModel.Factory(application)
        ).get(FirebaseViewModel::class.java)

        homeViewModel = ViewModelProvider(
            this,
            HomeViewModel.Factory(application)
        ).get(HomeViewModel::class.java)





        test_rv.run {
            setHasFixedSize(true)
//            layoutManager = GridLayoutManager(this@TestActivity, 1,GridLayoutManager.HORIZONTAL,false)
            layoutManager = LinearLayoutManager(this@TestActivity,LinearLayoutManager.HORIZONTAL,false)
            addItemDecoration(HorizonSpacingItemDecoration(50))
            adapter =mAdapter
        }




        mAdapter.setList(homeViewModel.getHomeItems().value!!,null)

        test_btn.setOnClickListener {
        }

    }


}
