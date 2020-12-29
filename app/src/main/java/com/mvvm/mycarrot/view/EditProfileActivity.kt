package com.mvvm.mycarrot.view

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ActivityEditProfileBinding
import com.mvvm.mycarrot.viewModel.HomeViewModel
import com.sucho.placepicker.AddressData
import com.sucho.placepicker.Constants
import com.sucho.placepicker.MapType
import com.sucho.placepicker.PlacePicker

class EditProfileActivity : AppCompatActivity() {

    lateinit var binding: ActivityEditProfileBinding
    lateinit var homeViewModel: HomeViewModel
    lateinit var customDialog: CustomProgressDialog
    var PICK_PROFILE_FROM_ALBUM = 1010
    lateinit var uri: Uri


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        customDialog = CustomProgressDialog(this)

        homeViewModel = ViewModelProvider(
            this,
            HomeViewModel.Factory(application)
        ).get(HomeViewModel::class.java)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        binding.apply {
            vm = homeViewModel
            av = this@EditProfileActivity
            lifecycleOwner = this@EditProfileActivity
        }

        initStatusBar()

    }


    fun getAlbum() {
        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_PROFILE_FROM_ALBUM)
    }

    fun clickFinishBtn(){
        customDialog.show()
        this::uri.isInitialized
    }

    /******************************************************************
     ******************************************************************
     ******************************************************************
     ******************************************************************
     ******************************************************************
     ******************************************************************
     *
     *
     *  EditProfileActivity 처음부터 다시 구조 짜서 할것.
     *  EditProfileViewModel 만들어야할것같음.
     *  profile이랑 nickname 때문에.
     *  닉네임 다 지우면 완료 버튼 비활성화도 하기.
     *
     *
     ******************************************************************
     ******************************************************************
     ******************************************************************
     ******************************************************************
     ******************************************************************
     ******************************************************************
     ******************************************************************
     ******************************************************************
     ******************************************************************
     ******************************************************************

     */



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_PROFILE_FROM_ALBUM && resultCode == Activity.RESULT_OK) {
            uri = data!!.data!!
        }
    }


    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }
}
