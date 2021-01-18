package com.mvvm.mycarrot.view.login

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.core.content.ContextCompat.getSystemService
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


        initViewModel()
        initBinding()
        initObserver()

        initListener()
        initCurrentLatLong()
        initRv()
    }

    private fun initListener() {
        binding.locationSettingEt.setOnEditorActionListener { textView, actionId, keyEvent ->
            if(actionId.and(EditorInfo.IME_MASK_ACTION)!=0){
                locationViewModel.setLikeQuery()
                keyboardHide()
            }
            true
        }
    }

    private fun keyboardHide() {
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.locationSettingEt.windowToken, 0)
    }


    fun clickCancel(){
        locationViewModel.searchString.postValue("")
        mAdapter.setList(listOf())
    }

    private fun initObserver() {
        locationViewModel.getLikeQuery().observe(this, Observer {
            mAdapter.setList(it)
        })

    }

    private fun initViewModel() {
        locationViewModel = ViewModelProvider(
            activity!!,
            LocationViewModel.Factory(activity!!.application)
        ).get(LocationViewModel::class.java)
    }

    private fun initBinding() {
        binding.apply {
            fm = this@LocationSettingFragment
            lifecycleOwner = this@LocationSettingFragment
            locationvm = locationViewModel
        }
    }

    fun clickCurrentLocation(){

        locationViewModel.setLocationQuery()
    }

    @SuppressLint("MissingPermission")
    private fun initCurrentLatLong() {
        var fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity!!)
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                if (location != null) {
                    locationViewModel.setCurrentLatLong(location.latitude, location.longitude)

                }
            }
    }

    fun startSetProfileFragment() {
        fragmentManager!!.beginTransaction()
            .setCustomAnimations(
                android.R.animator.fade_in,
                android.R.animator.fade_out,
                android.R.animator.fade_in,
                android.R.animator.fade_out
            )
            .add(R.id.login_fl, SetProfileFragment())
            .addToBackStack("setProfileFragment")
            .commit()
    }

    private fun initRv() {
        binding.locationSettingRv.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = mAdapter
        }

        mAdapter.listener = object:ItemRvLocationAdatper.ClickListener{
            override fun onClick(position: Int) {
                locationViewModel.setSelectedLocation(mAdapter.locationList[position])
                Log.d("fhrm", "LocationSettingFragment -onClick(),    locationViewModel.getSelectedLocation(): ${locationViewModel.getSelectedLocation()}")
                startSetProfileFragment()
            }

        }
    }
}