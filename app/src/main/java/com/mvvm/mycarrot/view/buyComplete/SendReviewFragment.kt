package com.mvvm.mycarrot.view.buyComplete


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.FragmentSendreviewBinding
import com.mvvm.mycarrot.viewModel.BuyCompleteViewModel

class SendReviewFragment : Fragment() {

    lateinit var binding: FragmentSendreviewBinding
    lateinit var buyCompleteViewModel: BuyCompleteViewModel
    var sendReviewFinishFragment = SendReviewFinishFragment()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sendreview, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewModel()
        initBinding()

    }

    private fun initBinding() {
        binding.apply {
            vm = buyCompleteViewModel
            lifecycleOwner = this@SendReviewFragment
            fm = this@SendReviewFragment
        }
    }

    private fun initViewModel() {
        buyCompleteViewModel =
            ViewModelProvider(activity!!, BuyCompleteViewModel.Factory(activity!!.application)).get(
                BuyCompleteViewModel::class.java
            )

        buyCompleteViewModel.getpositiveReviewList().observe(this, Observer {
            if (!binding.sendReviewRbNegative.isChecked) {
                if (it.isEmpty()) makeButtonGray()
                else makeButtonOrange()
            }
        })

        buyCompleteViewModel.getnegativeReviewList().observe(this, Observer {
            if (binding.sendReviewRbNegative.isChecked) {
                if (it.isEmpty()) makeButtonGray()
                else makeButtonOrange()
            }
        })
    }

    fun startSendReviewSecondFragment() {
        fragmentManager!!.beginTransaction()
            .setCustomAnimations(
                android.R.animator.fade_in,
                android.R.animator.fade_out,
                android.R.animator.fade_in,
                android.R.animator.fade_out
            )
            .add(R.id.buyComplete_fl, sendReviewFinishFragment)
            .addToBackStack("sendReviewFinishFragment")
            .commit()
    }

    fun makeButtonOrange() {
        binding.sendReviewTvNext.setBackgroundResource(R.color.colorOrange)
        binding.sendReviewTvNext.isEnabled = true
    }

    fun makeButtonGray() {
        binding.sendReviewTvNext.setBackgroundResource(R.color.colorDarkGray)
        binding.sendReviewTvNext.isEnabled = false
    }

    fun visiblePositiveLayout() {
        binding.sendReviewTvNext.visibility = View.VISIBLE
        binding.sendReviewClBestLike.visibility = View.VISIBLE
        binding.sendReviewClBad.visibility = View.INVISIBLE

        if (buyCompleteViewModel.getpositiveReviewList().value!!.isEmpty()) makeButtonGray()
        resetNegativeCheckBox()
    }

    fun visibleNegativeLayout() {
        binding.sendReviewTvNext.visibility = View.VISIBLE
        binding.sendReviewClBestLike.visibility = View.INVISIBLE
        binding.sendReviewClBad.visibility = View.VISIBLE

        if (buyCompleteViewModel.getnegativeReviewList().value!!.isEmpty()) makeButtonGray()
        resetPositiveCheckBox()
    }

    fun checkPositiveReview(str: String) {
        buyCompleteViewModel.setpositiveReviewList(str)
    }

    fun checkNegativeReview(str: String) {
        buyCompleteViewModel.setnegativeReviewList(str)
    }

    fun resetNegativeCheckBox() {
        buyCompleteViewModel.clearnegativeReviewList()
        binding.sendReviewCbNegative1.isChecked = false
        binding.sendReviewCbNegative2.isChecked = false
        binding.sendReviewCbNegative3.isChecked = false
        binding.sendReviewCbNegative4.isChecked = false
    }

    fun resetPositiveCheckBox() {
        buyCompleteViewModel.clearpositiveReviewList()
        binding.sendReviewCbPositive1.isChecked = false
        binding.sendReviewCbPositive2.isChecked = false
        binding.sendReviewCbPositive3.isChecked = false
        binding.sendReviewCbPositive4.isChecked = false
    }

    fun onBackPress() {
        fragmentManager!!.popBackStack()
    }


}
