package com.mvvm.mycarrot.view

import android.content.Intent
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
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.ItemRvAdapter
import com.mvvm.mycarrot.databinding.FragmentSeeMoreForSaleBinding
import com.mvvm.mycarrot.databinding.FragmentSeeMoreTotalBinding
import com.mvvm.mycarrot.viewModel.HomeViewModel

class SeeMoreTotalFragment : Fragment() {

    lateinit var homeViewModel: HomeViewModel
    lateinit var binding: FragmentSeeMoreTotalBinding
    lateinit var customDialog:CustomProgressDialog
    val itemRvAdapter = ItemRvAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_see_more_total, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customDialog = CustomProgressDialog(activity!!)

        homeViewModel = ViewModelProvider(
            activity!!, HomeViewModel.Factory(activity!!.application)
        ).get(HomeViewModel::class.java)

        binding.apply {
            lifecycleOwner = this@SeeMoreTotalFragment
        }

        homeViewModel.getIsStartItemActivity().observe(this, Observer { isStartActivity->
            if(isStartActivity ==2 && homeViewModel.getselectedFragment() == "seemoreTotalFm"){
                startItemActivity()
            }
        })


        initRv()
        initItemList()

    }

    private fun initItemList() {
        var list = homeViewModel.getselectedItemOwnersItem().value!!
        itemRvAdapter.setList(list)
    }

    private fun initRv() {
        binding.seemoreTotalRv.run{
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = itemRvAdapter
        }

        itemRvAdapter.listener = object : ItemRvAdapter.ClickListener{
            override fun onClick(position: Int) {
                beforeStartItemActivity(position)
            }
        }
    }

    fun beforeStartItemActivity(position: Int) {
        customDialog.show()
        homeViewModel.setselectedItem(itemRvAdapter.itemList[position].id!!,"seemoreTotalFm")
        homeViewModel.setselectedItemOwner(itemRvAdapter.itemList[position].userId!!,"seemoreTotalFm")
    }
    fun startItemActivity() {
        customDialog.dismiss()
        homeViewModel.clearIsStartItemActivity()
        startActivity(Intent(activity, ItemActivity::class.java))
    }
}