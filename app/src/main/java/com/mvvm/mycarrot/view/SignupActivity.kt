package com.mvvm.mycarrot.view

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.android.gms.location.LocationServices
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ActivitySignupBinding
import com.mvvm.mycarrot.viewModel.FirebaseViewModel
import com.sucho.placepicker.AddressData
import com.sucho.placepicker.Constants
import com.sucho.placepicker.MapType
import com.sucho.placepicker.PlacePicker
import kotlinx.android.synthetic.main.activity_signup.*

class SignupActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupBinding
    lateinit var viewModel: FirebaseViewModel


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            FirebaseViewModel.Factory(application)
        ).get(FirebaseViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_signup)
        binding.apply {
            lifecycleOwner = this@SignupActivity
            vm = viewModel
            av = this@SignupActivity
        }

        getLatLang()

        viewModel.getLocation().observe(this) { location ->
            binding.signupTvLocation.text = location
        }

    }

    // 등록완요 누를시 반영
    fun signup() {
        viewModel.signupUserObject()
        finish()
    }

    fun startActivityPlacePicker() {
        val intent = PlacePicker.IntentBuilder()
            .setLatLong(
                viewModel.firebaseRepository.lat,
                viewModel.firebaseRepository.long
            )
            .showLatLong(true)
            .setMapZoom(14.0f)
            .setAddressRequired(true)
            .hideMarkerShadow(true)
            .setMarkerImageImageColor(R.color.colorPrimary)
            .setMapType(MapType.NORMAL)
            .onlyCoordinates(true)
            .build(this)
        startActivityForResult(intent, Constants.PLACE_PICKER_REQUEST)
    }

    // PlacePicker에서 얻어온 LatLang 값으로 location 변경
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == Constants.PLACE_PICKER_REQUEST) {
            if (resultCode == Activity.RESULT_OK) {
                val addressData = data!!.getParcelableExtra<AddressData>(Constants.ADDRESS_INTENT)
                viewModel.setLatLong(addressData.latitude, addressData.longitude, application)
            }
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }


    // 현재 위치 GET
    private fun getLatLang() {

        // 권한 승인 안했을시 그냥 return
        if (ActivityCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this, android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }

        var fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    viewModel.setLatLong(location.latitude, location.longitude, application)
                }
            }
    }
}
