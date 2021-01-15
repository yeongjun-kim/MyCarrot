package com.mvvm.mycarrot.view.login

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.lifecycle.ViewModelProvider
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.TedPermission
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.room.DatabaseCopier
import com.mvvm.mycarrot.room.LocationViewModel
import com.mvvm.mycarrot.view.CustomLoadingDialog
import com.mvvm.mycarrot.view.CustomProgressDialog
import kotlinx.coroutines.*

class LoginActivity : AppCompatActivity() {

    lateinit var customDialog: CustomLoadingDialog
    lateinit var locationViewModel: LocationViewModel
    lateinit var job: Job

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login2)

        locationViewModel = ViewModelProvider(
            this,
            LocationViewModel.Factory(application)
        ).get(LocationViewModel::class.java)

        customDialog = CustomLoadingDialog(this)
        customDialog.show()


        initLocationDB()
        initStatusBar()
//        initDefaultFragment()
    }

    private fun initLocationDB() {
        job = CoroutineScope(Dispatchers.IO).launch {
            DatabaseCopier.copyAttachedDatabase(context = applicationContext)
        }

        runBlocking {
            job.join()
        }

    }

    private fun initDefaultFragment() {

        supportFragmentManager.beginTransaction()
            .add(R.id.login_fl, LoginFragment())
            .commit()
    }

    override fun onDestroy() {
        job.cancel()
        super.onDestroy()
    }


    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }
}
