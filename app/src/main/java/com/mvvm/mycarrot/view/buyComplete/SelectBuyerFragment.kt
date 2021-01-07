package com.mvvm.mycarrot.view.buyComplete

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.FragmentSelectbuyerBinding
import com.mvvm.mycarrot.test.fm2
import com.mvvm.mycarrot.viewModel.BuyCompleteViewModel

class SelectBuyerFragment : Fragment() {

    lateinit var binding: FragmentSelectbuyerBinding
    lateinit var buyCompleteViewModel: BuyCompleteViewModel


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_selectbuyer, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        buyCompleteViewModel =
            ViewModelProvider(activity!!, BuyCompleteViewModel.Factory(activity!!.application)).get(
                BuyCompleteViewModel::class.java
            )

        binding.apply {
            vm = buyCompleteViewModel
            lifecycleOwner = this@SelectBuyerFragment
            fm = this@SelectBuyerFragment
        }

    }


}
