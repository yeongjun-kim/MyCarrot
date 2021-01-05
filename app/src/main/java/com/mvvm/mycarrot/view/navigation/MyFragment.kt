package com.mvvm.mycarrot.view.navigation

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback

import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.FragmentMyBinding
import com.mvvm.mycarrot.view.*
import com.mvvm.mycarrot.viewModel.HomeViewModel

class MyFragment : Fragment(){


    lateinit var binding: FragmentMyBinding
    lateinit var homeViewModel: HomeViewModel
    lateinit var customDialog: CustomProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customDialog = CustomProgressDialog(activity!!)

        homeViewModel =
            ViewModelProvider(activity!!, HomeViewModel.Factory(activity!!.application)).get(
                HomeViewModel::class.java
            )

        binding.apply {
            lifecycleOwner = this@MyFragment
            fm = this@MyFragment
            vm = homeViewModel
        }


        homeViewModel.getIsStartItemActivity().observe(this, Observer { isStartActivity ->
            if (isStartActivity == 1 && homeViewModel.getselectedFragment() == "myFm") {
                startProfileActivity()
            }
        })

    }


    fun beforeStartProfileActivity() {
        customDialog.show()
        homeViewModel.setselectedItemOwner(homeViewModel.getCurrentUserObject().value!!.userId!!,"myFm")
    }


    fun startNeighborhoodCertificationActivity(){
        startActivity(Intent(activity, NeighborhoodCertificationActivity::class.java))
    }

    fun startCollectActivity(){
        startActivity(Intent(activity, CollectActivity::class.java))
    }

    fun startProfileActivity() {
        customDialog.dismiss()
        homeViewModel.clearIsStartItemActivity()
        startActivity(Intent(activity, ProfileActivity::class.java))
    }

    fun startSetupTownActivity(){
        startActivity(Intent(activity, SetupTownActivity::class.java))
    }

    fun startEditProfileActivity(){
        startActivity(Intent(activity, EditProfileActivity::class.java))
    }


}
