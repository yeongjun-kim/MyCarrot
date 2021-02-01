package com.mvvm.mycarrot.presentation.common

import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.data.db.LocationViewModel
import com.mvvm.mycarrot.presentation.write.WriteActivity
import com.mvvm.mycarrot.presentation.chat.ChatFragment
import com.mvvm.mycarrot.presentation.home.HomeFragment
import com.mvvm.mycarrot.presentation.my.MyFragment
import com.mvvm.mycarrot.presentation.search.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var homeFragment = HomeFragment()
    private var searchFragment = SearchFragment()
    private var chatFragment = ChatFragment()
    private var myFragment = MyFragment()
    private lateinit var locationViewModel: LocationViewModel

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .addToBackStack(null) // setCustomAnimations 이용할시, 빠른 화면전화면 에러나는 버그로 인해 추가
                        .replace(R.id.main_fl, homeFragment)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_search -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .addToBackStack(null)
                        .replace(R.id.main_fl, searchFragment)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_write -> {
                    val intent = Intent(this, WriteActivity::class.java)
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_chat -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .addToBackStack(null)
                        .replace(R.id.main_fl, chatFragment)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_my -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .addToBackStack(null)
                        .replace(R.id.main_fl, myFragment)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)



        initDefulatFragment()
        initViewModel()
        initStatusBar()
        initCurrentLocation()



        main_dummy_data.setOnClickListener {
            InsertDummyDataActivity()
        }
    }

    private fun initViewModel() {
        locationViewModel = ViewModelProvider(
            this,
            LocationViewModel.Factory(application)
        ).get(LocationViewModel::class.java)
    }

    /*
    유저의 현재위치를 저장
    */
    private fun initCurrentLocation() {
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
                    locationViewModel.setCurrentLatLong(location.latitude, location.longitude)
                }
            }
    }


    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }

    fun InsertDummyDataActivity() {
        startActivity(Intent(this, com.mvvm.mycarrot.test.InsertDummyDataActivity::class.java))
    }


    private fun initDefulatFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.main_fl, homeFragment)
            .commit()

    }


    private var doubleBackToExitPressedOnce = false
    override fun onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "한 번 더 누르면 종료됩니당.", Toast.LENGTH_SHORT).show()

        Handler().postDelayed(Runnable { doubleBackToExitPressedOnce = false }, 2000)
    }


}
