package com.mvvm.mycarrot.presentation.login

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.FragmentSetProfileBinding
import com.mvvm.mycarrot.data.db.LocationViewModel
import com.mvvm.mycarrot.presentation.common.CustomProgressDialog
import com.mvvm.mycarrot.presentation.common.MainActivity

class SetProfileFragment : Fragment() {

    lateinit var binding: FragmentSetProfileBinding
    lateinit var loginViewModel: LoginViewModel
    lateinit var locationViewModel: LocationViewModel
    lateinit var customDialog: CustomProgressDialog

    private var PICK_PROFILE_FROM_ALBUM = 1010

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_set_profile, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewModel()
        initBinding()
        initCustomDialog()
        initObserver()

    }

    private fun initObserver() {
        loginViewModel.getloginMode().observe(this, Observer { mode ->
            if (mode == 2) {
                customDialog.dismiss()
                startActivity(Intent(activity!!, MainActivity::class.java))
                activity!!.finish()
            }
        })
    }

    private fun initCustomDialog() {
        customDialog =
            CustomProgressDialog(activity!!)
    }

    private fun initViewModel() {
        loginViewModel =
            ViewModelProvider(activity!!, LoginViewModel.Factory(activity!!.application)).get(
                LoginViewModel::class.java
            )

        locationViewModel =
            ViewModelProvider(activity!!, LocationViewModel.Factory(activity!!.application)).get(
                LocationViewModel::class.java
            )

        loginViewModel.nickname.value = ""
        loginViewModel.profileImage = null
    }


    fun commitUser() {

        if (loginViewModel.nickname.value.isNullOrBlank() || loginViewModel.nickname.value!!.length < 3 || loginViewModel.profileImage == null) {
            Toast.makeText(activity, "정보를 올바르게 입력해주세용", Toast.LENGTH_SHORT).show()
        } else {
            customDialog.show()
            loginViewModel.commitUserObject(
                loginViewModel.nickname.value!!,
                locationViewModel.getSelectedLocation()!!,
                loginViewModel.profileImage!!
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == PICK_PROFILE_FROM_ALBUM && resultCode == Activity.RESULT_OK) {
            loginViewModel.profileImage = data!!.data
            setProfileImage(data!!.data!!)
        }
    }

    private fun setProfileImage(uri: Uri) {
        Glide.with(this).load(uri)
            .apply(RequestOptions().circleCrop())
            .transition(DrawableTransitionOptions().crossFade())
            .into(binding.setProfileIvProfile)
    }


    fun getAlbum() {
        var photoPickerIntent = Intent(Intent.ACTION_PICK)
        photoPickerIntent.type = "image/*"
        startActivityForResult(photoPickerIntent, PICK_PROFILE_FROM_ALBUM)
    }

    private fun initBinding() {
        binding.apply {
            lifecycleOwner = this@SetProfileFragment
            fm = this@SetProfileFragment
            loginVm = loginViewModel
        }
    }
}