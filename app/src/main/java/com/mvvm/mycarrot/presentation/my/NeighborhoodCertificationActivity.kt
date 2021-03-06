package com.mvvm.mycarrot.presentation.my

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
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
import com.mvvm.mycarrot.presentation.home.HomeViewModel
import kotlinx.android.synthetic.main.activity_neighborhood_certification.*

class NeighborhoodCertificationActivity : AppCompatActivity(), OnMapReadyCallback {

    private lateinit var binding: ActivityNeighborhoodCertificationBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initViewModel()
        initBinding()
        initStatusBar()
        initMap()
    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_neighborhood_certification)

        binding.apply {
            lifecycleOwner = this@NeighborhoodCertificationActivity
            av = this@NeighborhoodCertificationActivity
            vm = homeViewModel
        }
    }

    private fun initViewModel() {
        homeViewModel = ViewModelProvider(
            this, HomeViewModel.Factory(application)
        ).get(HomeViewModel::class.java)

        homeViewModel.getIsCertificationFinish().observe(this, Observer { isFinish ->
            if (isFinish) {
                homeViewModel.clearIsCertificationFinish()
                val dong = homeViewModel.getCurrentUserObject().value!!.location!!.split(" ")[1]
                Toast.makeText(this, "${dong} 동네인증 성공!", Toast.LENGTH_SHORT).show()
                finish()
            }
        })
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
        (neighborhoodCertification_fm_map as SupportMapFragment).getMapAsync(this)
    }

    fun doCertification() {
        homeViewModel.doCertification()
    }

    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }


}
