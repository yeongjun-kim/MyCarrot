package com.mvvm.mycarrot.view

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ActivityNeighborhoodCertificationBinding
import com.mvvm.mycarrot.viewModel.HomeViewModel
import kotlinx.android.synthetic.main.activity_neighborhood_certification.*

class NeighborhoodCertificationActivity : AppCompatActivity(), OnMapReadyCallback {

    lateinit var binding: ActivityNeighborhoodCertificationBinding
    lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_neighborhood_certification)
        homeViewModel = ViewModelProvider(
            this, HomeViewModel.Factory(application)
        ).get(HomeViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@NeighborhoodCertificationActivity
            av = this@NeighborhoodCertificationActivity
            vm = homeViewModel
        }

        homeViewModel.getIsCertificationFinish().observe(this, Observer {isFinish->
            if(isFinish){
                homeViewModel.clearIsCertificationFinish()
                val dong = homeViewModel.getCurrentUserObject().value!!.location!!.split(" ")[1]
                Toast.makeText(this,"${dong} 동네인증 성공!",Toast.LENGTH_SHORT).show()
                finish()
            }

        })

        initStatusBar()
        initMap()
    }



    override fun onMapReady(googleMap: GoogleMap?) {

        val currentLatLng = LatLng(
            homeViewModel.getCurrentLatLong().first,
            homeViewModel.getCurrentLatLong().second
        )

        val markerOptions = MarkerOptions()
        markerOptions.position(currentLatLng)
        googleMap!!.addMarker(markerOptions)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLatLng, 16f))
    }

    private fun initMap() {
        (neighborhood_certification_fm_map as SupportMapFragment).getMapAsync(this)
    }

    fun doCertification(){
        homeViewModel.doCertification()
    }

    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }


}
