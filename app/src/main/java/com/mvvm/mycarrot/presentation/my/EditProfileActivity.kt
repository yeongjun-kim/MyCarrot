package com.mvvm.mycarrot.presentation.my

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ActivityEditProfileBinding
import com.mvvm.mycarrot.presentation.common.CustomProgressDialog

class EditProfileActivity : AppCompatActivity() {

    private lateinit var binding: ActivityEditProfileBinding
    private lateinit var myViewModel: MyViewModel
    private lateinit var customDialog: CustomProgressDialog
    private var PICK_PROFILE_FROM_ALBUM = 1010

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        customDialog = CustomProgressDialog(this)
        initViewModel()
        initBinding()
        initStatusBar()
        initEt()

    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_edit_profile)
        binding.apply {
            vm = myViewModel
            av = this@EditProfileActivity
            lifecycleOwner = this@EditProfileActivity
        }

    }

    private fun initViewModel() {
        myViewModel = ViewModelProvider(
            this,
            MyViewModel.Factory(application)
        ).get(MyViewModel::class.java)

        myViewModel.isCommitFinish.observe(this, Observer { isFinish ->
            if (isFinish) {
                customDialog.dismiss()
                myViewModel.isCommitFinish.value = false
                finish()
            }
        })

    }

    private fun initEt() {
        binding.editProfileEtNickname.setText(myViewModel.getCurrentUserObject().value!!.nickname)
    }


    fun getAlbum() {
        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_PROFILE_FROM_ALBUM)
    }

    fun clickFinishBtn() {
        customDialog.show()
        myViewModel.commitChangeInfo()
    }


    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_PROFILE_FROM_ALBUM && resultCode == Activity.RESULT_OK) {
            myViewModel.newImageUri = data!!.data!!
            changeImageView(data.data!!)
        }
    }

    private fun changeImageView(data: Uri) {
        Glide.with(this)
            .load(data)
            .placeholder(R.drawable.ic_user)
            .circleCrop()
            .thumbnail(0.1f)
            .into(binding.editProfileIvProfile)
    }


    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }
}
