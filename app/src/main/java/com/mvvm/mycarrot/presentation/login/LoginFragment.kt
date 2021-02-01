package com.mvvm.mycarrot.presentation.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.FragmentLoginBinding
import com.mvvm.mycarrot.data.db.DatabaseCopier
import com.mvvm.mycarrot.data.db.LocationViewModel
import com.mvvm.mycarrot.presentation.common.MainActivity
import kotlinx.coroutines.*

class LoginFragment : Fragment() {


    private val GOOGLE_SIGNIN_CODE = 9001
    private lateinit var binding: FragmentLoginBinding
    private lateinit var locationViewModel: LocationViewModel
    private lateinit var loginViewModel: LoginViewModel
    private lateinit var job: Job



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


        initViewModel()
        initBinding()
        initLocationDB()
        initObserver()



    }

    private fun initObserver() {
        loginViewModel.getisExistAccount().observe(this, Observer { isExistAccount ->
            if(isExistAccount==1){ // 새로 가입해야함
                startLocationSettingFragment()
            }else if(isExistAccount==2){ // 이미 가입한 계정 존재
                startMainActivity()
            }
        })
    }

    private fun startMainActivity() {
        startActivity(Intent(activity!!, MainActivity::class.java))
        activity!!.finish()
    }

    private fun initBinding() {
        binding.apply {
            lifecycleOwner = this@LoginFragment
            fm = this@LoginFragment
        }
    }

    private fun initViewModel() {
        locationViewModel = ViewModelProvider(
            activity!!,
            LocationViewModel.Factory(activity!!.application)
        ).get(LocationViewModel::class.java)

        loginViewModel= ViewModelProvider(
            activity!!,
            LoginViewModel.Factory(activity!!.application)
        ).get(LoginViewModel::class.java)
    }

    private fun initLocationDB() {
        job = CoroutineScope(Dispatchers.IO).launch {
            DatabaseCopier.copyAttachedDatabase(context = activity!!.applicationContext)
        }

        runBlocking {
            job.join()
        }
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == GOOGLE_SIGNIN_CODE) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                loginViewModel.firebaseAuthWithGoogle(account.idToken!!)
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
            .add(R.id.login_fl,
                LocationSettingFragment()
            )
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
        startActivityForResult(signInIntent, GOOGLE_SIGNIN_CODE)
    }
}