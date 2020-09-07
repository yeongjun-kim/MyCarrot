package com.mvvm.mycarrot.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ActivityWriteBinding

class WriteActivity : AppCompatActivity() {

    lateinit var binding: ActivityWriteBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_write)
        binding.apply {
            activitiy = this@WriteActivity
        }

    }

    override fun finish() {
        super.finish()
    }

    fun commit(){

    }
}
