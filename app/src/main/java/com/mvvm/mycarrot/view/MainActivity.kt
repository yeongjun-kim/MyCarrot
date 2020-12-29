package com.mvvm.mycarrot.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.test.TestActivity
import com.mvvm.mycarrot.test.TestChatActivity
import com.mvvm.mycarrot.test.TestFragment
import com.mvvm.mycarrot.view.navigation.ChatFragment
import com.mvvm.mycarrot.view.navigation.HomeFragment
import com.mvvm.mycarrot.view.navigation.MyFragment
import com.mvvm.mycarrot.view.navigation.SearchFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var homeFragment = HomeFragment()
    var searchFragment = SearchFragment()
    var chatFragment = ChatFragment()
    var myFragment = MyFragment()
    var testFragment = TestFragment()

    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {

                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(
                            R.anim.fade_in,
                            R.anim.fade_out
                        )
                        .addToBackStack(null) // setCustomAnimations 이용할시, 빠른 화면전화면 에러나는 버그로 인해 추가
                        .replace(R.id.main_fl, homeFragment)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_search -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(
                            R.anim.fade_in,
                            R.anim.fade_out
                        )
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
                        .setCustomAnimations(
                            R.anim.fade_in,
                            R.anim.fade_out
                        )
                        .addToBackStack(null)
                        .replace(R.id.main_fl, chatFragment)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_my -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(
                            R.anim.fade_in,
                            R.anim.fade_out
                        )
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
        val navView: BottomNavigationView = findViewById(com.mvvm.mycarrot.R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)



        initDefulatFragment()
        initStatusBar()



        test_btn1.setOnClickListener {
            test()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d("fhrm", "MainActivity -onDestroy(),    : destroy")
    }

    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }

    fun test() {
        startActivity(Intent(this, TestActivity::class.java))
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
