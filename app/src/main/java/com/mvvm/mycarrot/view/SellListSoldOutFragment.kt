package com.mvvm.mycarrot.view

import android.content.Intent
import android.os.Bundle
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
import com.mvvm.mycarrot.adapter.ItemRvSellListAdapter
import com.mvvm.mycarrot.databinding.FragmentSellListSoldOutBinding
import com.mvvm.mycarrot.viewModel.MyViewModel

class SellListSoldOutFragment : Fragment() {

    lateinit var binding: FragmentSellListSoldOutBinding
    lateinit var myViewModel: MyViewModel
    lateinit var customDialog:CustomProgressDialog
    var itemRvSoldoutListAdapter = ItemRvAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sell_list_sold_out,
            container,
            false
        )
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customDialog = CustomProgressDialog(activity!!)

        myViewModel = ViewModelProvider(activity!!, MyViewModel.Factory(activity!!.application)).get(MyViewModel::class.java)
        myViewModel.getmyItemList().observe(this, Observer { input ->

            val itemList = input.filter { it.status=="soldout" }

            itemRvSoldoutListAdapter.setList(itemList)

            // 아이템이 있을땐 [거래완료 게시글이 없어요]TextView INVISIBLE
            if(itemList.isNotEmpty()){
                binding.sellListSoldoutTv.visibility = View.INVISIBLE
            }else binding.sellListSoldoutTv.visibility = View.VISIBLE
        })

        myViewModel.getIsStartItemActivity().observe(this, Observer { isStartActivity->
            if(isStartActivity ==2 && myViewModel.getselectedFragment() == "soldOutFm"){
                startItemActivity()
            }
        })


        initRv()

    }

    fun initRv(){
        binding.sellListSoldoutRv.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = itemRvSoldoutListAdapter
        }

        itemRvSoldoutListAdapter.listener = object :ItemRvAdapter.ClickListener{
            override fun onClick(position: Int) {
                customDialog.show()
                myViewModel.setselectedItem(itemRvSoldoutListAdapter.itemList[position].id!!, "soldOutFm")
                myViewModel.setselectedItemOwner(itemRvSoldoutListAdapter.itemList[position].userId!!, "soldOutFm")
            }
        }
    }

    fun startItemActivity(){
        customDialog.dismiss()
        myViewModel.clearIsStartItemActivity()
        startActivity(Intent(activity,ItemActivity::class.java))
    }

}
