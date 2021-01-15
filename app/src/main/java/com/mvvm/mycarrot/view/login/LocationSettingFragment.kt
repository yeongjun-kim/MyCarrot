package com.mvvm.mycarrot.view.login

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.LocationServices
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.ItemRvAdapter
import com.mvvm.mycarrot.adapter.ItemRvLocationAdatper
import com.mvvm.mycarrot.databinding.FragmentLocationSettingBinding
import com.mvvm.mycarrot.room.LocationViewModel
import com.mvvm.mycarrot.viewModel.FirebaseViewModel

class LocationSettingFragment : Fragment() {


    lateinit var binding: FragmentLocationSettingBinding
    lateinit var viewModel: FirebaseViewModel
    lateinit var locationViewModel: LocationViewModel
    private var mAdapter = ItemRvLocationAdatper()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_location_setting, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        binding.apply {
            fm = this@LocationSettingFragment
            lifecycleOwner = this@LocationSettingFragment
        }

        viewModel = ViewModelProvider(
            this,
            FirebaseViewModel.Factory(activity!!.application)
        ).get(FirebaseViewModel::class.java)

        locationViewModel = ViewModelProvider(
            this,
            LocationViewModel.Factory(activity!!.application)
        ).get(LocationViewModel::class.java)

        locationViewModel.getLikeQuery().observe(this, Observer {
            it.forEach { Log.d("fhrm", "TestRoomActivity -onCreate(),    : ${it.str}") }
        })


        initCurrentLatLang()
        initRv()
    }

    @SuppressLint("MissingPermission")
    private fun initCurrentLatLang() {
        var fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if(location!=null){

                }
//                if (location != null) {
//                    viewModel.setLatLong(location.latitude, location.longitude, activity!!.application)
//                }
            }
    }

    private fun initRv() {
        binding.locationSettingRv.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter
        }

        mAdapter.listener = object:ItemRvLocationAdatper.ClickListener{
            override fun onClick(position: Int) {
                Log.d("fhrm", "LocationSettingFragment -onClick(),    : click")
            }

        }
    }
}