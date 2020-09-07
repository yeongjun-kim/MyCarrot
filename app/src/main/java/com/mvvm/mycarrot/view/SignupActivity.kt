package com.mvvm.mycarrot.view

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ActivitySignupBinding
import com.mvvm.mycarrot.viewModel.FirebaseViewModel

class SignupActivity : AppCompatActivity() {

    lateinit var binding: ActivitySignupBinding
    lateinit var viewModel: FirebaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            FirebaseViewModel.Factory(application)
        ).get(FirebaseViewModel::class.java)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_signup)
        binding.apply {
            lifecycleOwner = this@SignupActivity
            vm = viewModel
            av = this@SignupActivity
        }
    }

    fun signup(){
        viewModel.signupUserObject()
        finish()
    }
}
