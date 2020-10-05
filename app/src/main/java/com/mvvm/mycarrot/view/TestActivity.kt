package com.mvvm.mycarrot.view

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.google.android.gms.location.LocationServices
import com.google.firebase.firestore.*
import com.google.firebase.storage.FirebaseStorage
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.OwnerItemRvAdapter
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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        viewModel = ViewModelProvider(
            this,
            FirebaseViewModel.Factory(application)
        ).get(FirebaseViewModel::class.java)



        test_btn.setOnClickListener {
            homeViewModel.test()
        }

    }


}
