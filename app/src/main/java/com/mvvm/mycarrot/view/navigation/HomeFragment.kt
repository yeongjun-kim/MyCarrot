package com.mvvm.mycarrot.view.navigation

import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider

import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.FragmentHomeBinding
import com.mvvm.mycarrot.viewModel.FirebaseViewModel

class HomeFragment : Fragment() {

    lateinit var firebaseViewModel: FirebaseViewModel
    lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_home, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        firebaseViewModel = ViewModelProvider(
            this, FirebaseViewModel.Factory(activity!!.application)
        ).get(FirebaseViewModel::class.java)

        binding.apply {
            fbVm = firebaseViewModel
            fm = this@HomeFragment
            lifecycleOwner = this@HomeFragment
        }

        Log.d("fhrm", "HomeFragment -onActivityCreated(),    : ${firebaseViewModel.getLocation().value}")
    }
}
