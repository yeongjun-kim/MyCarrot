package com.mvvm.mycarrot.presentation.buyComplete

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
import com.mvvm.mycarrot.adapter.recyclerView.ItemRvBuyerAdapter
import com.mvvm.mycarrot.databinding.FragmentSelectbuyerBinding

class SelectBuyerFragment : Fragment() {

    lateinit var binding: FragmentSelectbuyerBinding
    lateinit var buyCompleteViewModel: BuyCompleteViewModel
    private var sendReviewFragment =
        SendReviewFragment()
    var itemRvBuyerAdapter =
        ItemRvBuyerAdapter()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_selectbuyer, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewModel()
        initBinding()
        initRv()

    }

    private fun initBinding() {
        binding.apply {
            vm = buyCompleteViewModel
            lifecycleOwner = this@SelectBuyerFragment
            fm = this@SelectBuyerFragment
        }
    }

    private fun initViewModel() {
        buyCompleteViewModel =
            ViewModelProvider(activity!!, BuyCompleteViewModel.Factory(activity!!.application)).get(
                BuyCompleteViewModel::class.java
            )
        buyCompleteViewModel.getbuyCompleteChatList().observe(this, Observer { list ->
            list.forEach {
                itemRvBuyerAdapter.setList(list)
            }
        })
    }


    private fun initRv() {
        binding.selectBuyerRv.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = itemRvBuyerAdapter
        }

        itemRvBuyerAdapter.listener = object : ItemRvBuyerAdapter.ClickListener {
            override fun onClick(position: Int) {
                var buyerUid =
                    if (buyCompleteViewModel.getCurrentUserObject().value!!.userId == itemRvBuyerAdapter.messageList[position].myUid) itemRvBuyerAdapter.messageList[position].yourUid
                    else itemRvBuyerAdapter.messageList[position].myUid
                buyCompleteViewModel.setselectedBuyer(buyerUid)
                startSendReviewFragment()

            }
        }
    }


    private fun startSendReviewFragment() {
        fragmentManager!!.beginTransaction()
            .setCustomAnimations(
                android.R.animator.fade_in,
                android.R.animator.fade_out,
                android.R.animator.fade_in,
                android.R.animator.fade_out
            )
            .add(R.id.buyComplete_fl, sendReviewFragment)
            .addToBackStack("sendReviewFragment")
            .commit()
    }

    fun finishActivity() {
        activity!!.finish()
    }


}
