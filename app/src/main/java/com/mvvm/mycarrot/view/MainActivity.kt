package com.mvvm.mycarrot.view

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.view.View
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.view.navigation.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {

    var homeFragment = HomeFragment()
    var searchFragment = SearchFragment()
    var chatFragment = ChatFragment()
    var myFragment = MyFragment()
    var seeMoreFragment = SeeMoreFragment()
    var isFromItemActivity = false

    private lateinit var textMessage: TextView
    private val onNavigationItemSelectedListener =
        BottomNavigationView.OnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    var currentFragment = supportFragmentManager.findFragmentById(R.id.main_fl)
                    if (isFromItemActivity && currentFragment==seeMoreFragment) destroyBackStackAndStartMainActivity()


                    lateinit var replaceFragment: Fragment
                    if (isFromItemActivity) replaceFragment = seeMoreFragment
                    else replaceFragment = homeFragment

                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(
                            R.anim.fade_in,
                            R.anim.fade_out
                        )
                        .replace(R.id.main_fl, replaceFragment)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
                R.id.navigation_search -> {
                    supportFragmentManager.beginTransaction()
                        .setCustomAnimations(
                            R.anim.fade_in,
                            R.anim.fade_out
                        )
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
                        .replace(R.id.main_fl, myFragment)
                        .commit()
                    return@OnNavigationItemSelectedListener true
                }
            }
            false
        }


    /*
    ItemActivity -> SeeMoreFragment( MainActivity )-> navigation_home 이동 시
    Backstack 모두 지우고
     */
    private fun destroyBackStackAndStartMainActivity() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        this@MainActivity.finish()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val navView: BottomNavigationView = findViewById(com.mvvm.mycarrot.R.id.nav_view)
        navView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        isFromItemActivity = intent.getBooleanExtra("fromItemActivity", false)


        testtest.setOnClickListener { test() }

        initDefulatFragment()
        initStatusBar()
    }

    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }

    fun test() {
    }


    private fun initDefulatFragment() {
        if (isFromItemActivity) {
            supportFragmentManager.beginTransaction()
                .add(R.id.main_fl, seeMoreFragment)
                .commit()
        } else {
            supportFragmentManager.beginTransaction()
                .add(R.id.main_fl, homeFragment)
                .commit()
        }
    }
}
