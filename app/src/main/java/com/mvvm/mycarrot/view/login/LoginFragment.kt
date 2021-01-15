package com.mvvm.mycarrot.view.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.observe
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.FragmentLoginBinding
import com.mvvm.mycarrot.view.LoginActivity
import com.mvvm.mycarrot.view.MainActivity
import com.mvvm.mycarrot.view.SignupActivity
import com.mvvm.mycarrot.viewModel.FirebaseViewModel

class LoginFragment : Fragment() {


    val GOOGLE_SIGNIN_CODE = 9001
    lateinit var binding: FragmentLoginBinding
    lateinit var viewModel: FirebaseViewModel


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_login, container, false)
        return binding.root
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {
            lifecycleOwner = this@LoginFragment
            fm = this@LoginFragment
        }

        viewModel = ViewModelProvider(
            this,
            FirebaseViewModel.Factory(activity!!.application)
        ).get(FirebaseViewModel::class.java)

        viewModel.getLoginMode().observe(this) { loginMode ->
            viewModel.clear()
            if (loginMode == 1) {
                Log.d("fhrm", "LoginFragment -onActivityCreated(),    : mode1")
                startLocationSettingFragment()
            } else if (loginMode == 2) {
                startActivity(Intent(activity, MainActivity::class.java))
                activity!!.finish()
            }
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

    fun setPermission() {
        val permission = object : PermissionListener {
            override fun onPermissionGranted() {
                startGoogleLogin()
            }
            override fun onPermissionDenied(deniedPermissions: MutableList<String>?) {
                Toast.makeText(activity, "권한을 허용해주세용", Toast.LENGTH_SHORT).show()
            }
        }

        TedPermission.with(activity)
            .setPermissionListener(permission)
            .setRationaleMessage("현재 위치를 알기 위해 권한을 허용해주세요 당근당근")
            .setDeniedMessage("거절하면 위치 못써요 당근당근")
            .setPermissions(
                android.Manifest.permission.ACCESS_COARSE_LOCATION,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            .check()

    }

    fun startLocationSettingFragment() {
        fragmentManager!!.beginTransaction()
            .setCustomAnimations(
                android.R.animator.fade_in,
                android.R.animator.fade_out,
                android.R.animator.fade_in,
                android.R.animator.fade_out
            )
            .add(R.id.login_fl, LocationSettingFragment())
            .addToBackStack("locationSettingFragment")
            .commit()
    }

    fun startGoogleLogin() {
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        val googleSignInClient = GoogleSignIn.getClient(activity!!, gso)
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, LoginActivity.GOOGLE_SIGNIN_CODE)
    }
}