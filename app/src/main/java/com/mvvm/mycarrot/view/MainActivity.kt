package com.mvvm.mycarrot.view

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.view.navigation.ChatFragment
import com.mvvm.mycarrot.view.navigation.HomeFragment
import com.mvvm.mycarrot.view.navigation.MyFragment
import com.mvvm.mycarrot.view.navigation.SearchFragment
import com.mvvm.mycarrot.viewModel.FirebaseViewModel

class MainActivity : AppCompatActivity() {

    var homeFragment = HomeFragment()
    var searchFragment = SearchFragment()
    var chatFragment = ChatFragment()
    var myFragment = MyFragment()

    private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.main_fl, homeFragment)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_search -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.main_fl, searchFragment)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_write -> {
                    Log.d("fhrm", "MainActivity -(),    write")
                    val intent = Intent(this, WriteActivity::class.java)
                    startActivity(intent)
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_chat -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                        .replace(R.id.main_fl, chatFragment)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_my -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
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
    }

    fun test(){

    }


    private fun initDefulatFragment() {
        supportFragmentManager.beginTransaction()
            .add(R.id.main_fl, homeFragment)
            .commit()
    }
}
