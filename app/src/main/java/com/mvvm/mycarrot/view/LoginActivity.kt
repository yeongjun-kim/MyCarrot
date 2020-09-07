package com.mvvm.mycarrot.view

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ActivityLoginBinding
import com.mvvm.mycarrot.viewModel.FirebaseViewModel

class LoginActivity : AppCompatActivity() {

    lateinit var binding: ActivityLoginBinding
    lateinit var viewModel: FirebaseViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.apply {
            activitiy = this@LoginActivity
        }
        viewModel = ViewModelProvider(
            this,
            FirebaseViewModel.Factory(application)
        ).get(FirebaseViewModel::class.java)


        viewModel.getCurrentUser().observe(this) {
            Log.d("fhrm", "LoginActivity -onCreate(),    : ")
        }

        viewModel.getLoginMode().observe(this) { loginMode ->
            viewModel.clear()
            if (loginMode == 1) startActivity(Intent(this, SignupActivity::class.java))
            else if (loginMode == 2) startActivity(Intent(this, MainActivity::class.java))
        }

        binding.test.setOnClickListener {
            var a= ViewModelProvider(
                this,
                FirebaseViewModel.Factory(application)
            ).get(FirebaseViewModel::class.java)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGNIN_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                viewModel.firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Log.w("fhrm", "Google sign in failed", e)
            }
        }
    }

    fun signInGoogle() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(this, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, GOOGLE_SIGNIN_CODE)
    }


    companion object {
        const val GOOGLE_SIGNIN_CODE = 9001
    }
}
