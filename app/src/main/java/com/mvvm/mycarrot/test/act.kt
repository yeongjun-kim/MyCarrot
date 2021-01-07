package com.mvvm.mycarrot.test

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.mvvm.mycarrot.R

class act:AppCompatActivity(){

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.test_act)

        var fm1 = fm1()

        supportFragmentManager.beginTransaction()
            .add(R.id.test_act_fl, fm1)
            .addToBackStack("fm1")
            .commit()
    }
}