package com.mvvm.mycarrot.view

import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.location.Address
import android.location.Geocoder
import android.location.Location
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.LocationServices
import com.mvvm.mycarrot.R
import com.sucho.placepicker.AddressData
import com.sucho.placepicker.Constants
import com.sucho.placepicker.MapType
import com.sucho.placepicker.PlacePicker
import kotlinx.android.synthetic.main.activity_test.*
import java.util.*

class TestActivity : AppCompatActivity() {

    var lat = 37.55
    var long = 126.97

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)


        test_btn.setOnClickListener { startActivityPlacePicker() }
        getLatLang()


    }

    private fun getLatLang() {
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
                    lat = location.latitude
                    long = location.longitude
                }
            }.addOnFailureListener {
                Log.d("fhrm", "TestActivity -getLatLang(),    error: ${it.message}")
            }

    }

    private fun startActivityPlacePicker() {
        val intent = PlacePicker.IntentBuilder()
            .setLatLong(
                lat,
                long
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



    fun latlngToLocation(inputLat: Double = 37.55, inputLong: Double = 126.97): String {
        var mGeocoder = Geocoder(this, Locale.KOREAN)
        var mResultList: List<Address>?

        mResultList = mGeocoder.getFromLocation(inputLat, inputLong, 1)
        if (!mResultList.isNullOrEmpty()) return mResultList[0].getAddressLine(0).substring(5)
        else return ""
    }
}
