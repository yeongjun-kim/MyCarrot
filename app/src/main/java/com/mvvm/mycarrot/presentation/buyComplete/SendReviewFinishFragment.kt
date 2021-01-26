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
import com.mvvm.mycarrot.adapter.recyclerView.ItemRvReviewAdapter
import com.mvvm.mycarrot.databinding.FragmentSendreviewFinishBinding
import com.mvvm.mycarrot.presentation.common.CustomProgressDialog

class SendReviewFinishFragment : Fragment() {

    lateinit var binding: FragmentSendreviewFinishBinding
    lateinit var buyCompleteViewModel: BuyCompleteViewModel
    lateinit var customDialog: CustomProgressDialog
    private var itemRvReviewAdapter =
        ItemRvReviewAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_sendreview_finish, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        customDialog =
            CustomProgressDialog(activity!!)

        initViewModel()
        initBinding()
        initRv()
    }

    private fun initBinding() {
        binding.apply {
            vm = buyCompleteViewModel
            lifecycleOwner = this@SendReviewFinishFragment
            fm = this@SendReviewFinishFragment
        }
    }

    private fun initViewModel() {
        buyCompleteViewModel =
            ViewModelProvider(activity!!, BuyCompleteViewModel.Factory(activity!!.application)).get(
                BuyCompleteViewModel::class.java
            )
        buyCompleteViewModel.getpositiveReviewList().observe(this, Observer { list ->
            if (list.isNotEmpty()) {
                itemRvReviewAdapter.reviewList = list
            }
        })

        buyCompleteViewModel.getnegativeReviewList().observe(this, Observer { list ->
            if (list.isNotEmpty()) {
                itemRvReviewAdapter.reviewList = list
            }
        })

        buyCompleteViewModel.getisCommitFinish().observe(this, Observer { isFinish ->
            if (isFinish) finishActivity()
        })
    }

    fun onBackPress() {
        fragmentManager!!.popBackStack()
    }

    private fun initRv() {
        binding.sendReviewFinishRv.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = itemRvReviewAdapter
        }
    }

    fun completeSendReview() {
        customDialog.show()
        buyCompleteViewModel.commitReviewToServer()

    }

    fun finishActivity() {
        customDialog.dismiss()
        activity!!.finish()
    }
}
