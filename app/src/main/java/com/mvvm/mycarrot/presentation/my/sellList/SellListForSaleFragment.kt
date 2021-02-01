package com.mvvm.mycarrot.presentation.my.sellList

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
import com.mvvm.mycarrot.adapter.recyclerView.ItemRvSellListAdapter
import com.mvvm.mycarrot.databinding.FragmentSellListForSaleBinding
import com.mvvm.mycarrot.presentation.common.CustomProgressDialog
import com.mvvm.mycarrot.presentation.common.ItemActivity
import com.mvvm.mycarrot.presentation.buyComplete.BuyCompleteActivity
import com.mvvm.mycarrot.presentation.my.MyViewModel

class SellListForSaleFragment : Fragment() {

    private lateinit var binding: FragmentSellListForSaleBinding
    private lateinit var myViewModel: MyViewModel
    private lateinit var customDialog: CustomProgressDialog

    var itemRvSellListAdapter =
        ItemRvSellListAdapter()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_sell_list_for_sale,
            container,
            false
        )

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customDialog =
            CustomProgressDialog(activity!!)

        initViewModel()
        initRv()
    }

    private fun initViewModel() {
        myViewModel =
            ViewModelProvider(activity!!, MyViewModel.Factory(activity!!.application)).get(
                MyViewModel::class.java
            )
        myViewModel.getmyItemList().observe(this, Observer { input ->

            val itemList = input.filter { it.status != "soldout" }

            itemRvSellListAdapter.setList(itemList)

            // 아이템이 있을땐 [판매중인 게시글이 없어요]TextView INVISIBLE
            if (itemList.isNotEmpty()) {
                binding.sellListForSaleTv.visibility = View.INVISIBLE
            } else binding.sellListForSaleTv.visibility = View.VISIBLE
        })


        myViewModel.getIsStartItemActivity().observe(this, Observer { isStartActivity ->
            if (isStartActivity == 2 && myViewModel.getselectedFragment() == "forSaleFm") {
                startItemActivity()
            }
        })
    }

    private fun initRv() {
        binding.sellListForSaleRv.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = itemRvSellListAdapter
        }

        itemRvSellListAdapter.listener = object : ItemRvSellListAdapter.ClickListener {
            override fun onClClick(position: Int) {
                customDialog.show()
                myViewModel.setselectedItem(
                    itemRvSellListAdapter.itemList[position].id!!,
                    "forSaleFm"
                )
                myViewModel.setselectedItemOwner(
                    itemRvSellListAdapter.itemList[position].userId!!,
                    "forSaleFm"
                )
            }

            override fun onReservationClick(position: Int) {
                val itemId = itemRvSellListAdapter.itemList[position].id!!

                if (itemRvSellListAdapter.itemList[position].status == "reservation") myViewModel.changeItemStatus(
                    itemId,
                    "sell"
                )
                else myViewModel.changeItemStatus(itemId, "reservation")

            }

            override fun onSoldoutClick(position: Int) {
                val itemId = itemRvSellListAdapter.itemList[position].id!!
                myViewModel.changeItemStatus(itemId, "soldout")
                startBuycompleteActivity(itemId)
            }
        }
    }

    fun startItemActivity() {
        customDialog.dismiss()
        myViewModel.clearIsStartItemActivity()
        startActivity(Intent(activity, ItemActivity::class.java))
    }

    fun startBuycompleteActivity(itemId: String) {
        var intent = Intent(activity, BuyCompleteActivity::class.java)
        intent.putExtra("itemId", itemId)
        startActivity(intent)
    }
}
