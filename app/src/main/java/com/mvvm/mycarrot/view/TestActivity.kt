package com.mvvm.mycarrot.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.viewModel.FirebaseViewModel
import com.sucho.placepicker.AddressData
import com.sucho.placepicker.Constants
import com.sucho.placepicker.MapType
import com.sucho.placepicker.PlacePicker
import kotlinx.android.synthetic.main.activity_test.*
import java.util.*

class TestActivity : AppCompatActivity() {

    val PICK_PROFILE_FROM_ALBUM = 12
    var uri: Uri? = null
    var result:String?  = null
    lateinit var viewModel:FirebaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


         viewModel = ViewModelProvider(this,FirebaseViewModel.Factory(application)).get(FirebaseViewModel::class.java)

        test_btn1.setOnClickListener {  }
        test_btn2.setOnClickListener {
        }
        test_btn3.setOnClickListener {
        }


    }

}
