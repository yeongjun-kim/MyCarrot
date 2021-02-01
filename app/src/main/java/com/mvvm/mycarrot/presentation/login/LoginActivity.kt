package com.mvvm.mycarrot.presentation.login

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.presentation.common.CustomLoadingDialog
import com.mvvm.mycarrot.presentation.common.MainActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var customDialog: CustomLoadingDialog
    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        initLoadingDialog()
        initViewModel()
        initStatusBar()
        initObserver()

    }


    private fun initObserver() {
        loginViewModel.getloginMode().observe(this, Observer { mode ->
            if (mode == 1) { // GoogleAuth 로그인 실패 || GoogleAuth 로그인 성공, Firstore 실패
                initLoginFragment()
            } else if (mode == 2) { // GoogleAuth 성공 && Firestore 성공
                startMainActivity()
            }
        })
    }


    private fun startMainActivity() {
        customDialog.dismiss()
        startActivity(Intent(this, MainActivity::class.java))
        this@LoginActivity.finish()
    }

    private fun initLoadingDialog() {
        customDialog =
            CustomLoadingDialog(this)
        customDialog.show()
    }


    private fun initViewModel() {
        loginViewModel = ViewModelProvider(
            this,
            LoginViewModel.Factory(application)
        ).get(LoginViewModel::class.java)
    }


    private fun initLoginFragment() {
        customDialog.dismiss()
        supportFragmentManager.beginTransaction()
            .add(
                R.id.login_fl,
                LoginFragment()
            )
            .commit()
    }

    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }
}
