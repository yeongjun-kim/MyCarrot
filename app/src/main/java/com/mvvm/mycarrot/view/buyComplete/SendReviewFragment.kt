package com.mvvm.mycarrot.view.buyComplete


import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.FragmentSendreviewBinding
import com.mvvm.mycarrot.viewModel.BuyCompleteViewModel
import kotlinx.android.synthetic.main.fragment_sendreview.*

class SendReviewFragment : Fragment() {

    lateinit var binding: FragmentSendreviewBinding
    lateinit var buyCompleteViewModel: BuyCompleteViewModel
    val positiveList = MutableLiveData<List<String>>(listOf())
    val negativeList = MutableLiveData<List<String>>(listOf())


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_sendreview, container, false)
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
            lifecycleOwner = this@SendReviewFragment
            fm = this@SendReviewFragment
        }

        buyCompleteViewModel.getselectedBuyer().observe(this, Observer { it ->
            Log.d("fhrm", "SendReviewFragment -onActivityCreated(),    it.nickname: ${it.nickname}")
        })

        positiveList.observe(this, Observer {
            if(it.isEmpty()) {
                binding.sendReviewTvNext.setBackgroundResource(R.color.colorDarkGray)
                binding.sendReviewTvNext.isEnabled = false
            }
            else {
                binding.sendReviewTvNext.setBackgroundResource(R.color.colorOrange)
                binding.sendReviewTvNext.isEnabled = true
            }
        })

        negativeList.observe(this, Observer {
            if(it.isEmpty()) {
                binding.sendReviewTvNext.setBackgroundResource(R.color.colorDarkGray)
                binding.sendReviewTvNext.isEnabled = false
            }
            else {
                binding.sendReviewTvNext.setBackgroundResource(R.color.colorOrange)
                binding.sendReviewTvNext.isEnabled = true
            }
        })
    }

    fun startSendReviewSecondFragment(){
        /****************
         ****************
         *
         * 여기부터 구현하면됨. 두번째 거래후기 fragment부터 시작하면됨.
         *
         ****************
         ****************
         ****************
         */
    }

    fun visiblePositiveLayout() {
        binding.sendReviewTvNext.visibility = View.VISIBLE
        binding.sendReviewClBestLike.visibility = View.VISIBLE
        binding.sendReviewClBad.visibility = View.INVISIBLE

        resetNegativeCheckBox()
    }

    fun visibleNegativeLayout() {
        binding.sendReviewTvNext.visibility = View.VISIBLE
        binding.sendReviewClBestLike.visibility = View.INVISIBLE
        binding.sendReviewClBad.visibility = View.VISIBLE

        resetPositiveCheckBox()
    }

    fun checkPositiveReview(str: String) {
        var tempList = positiveList.value!!.toMutableList()

        if (positiveList.value!!.contains(str)) {
            tempList.remove(str)
            positiveList.value = tempList.toList()
        } else {
            tempList.add(str)
            positiveList.value = tempList.toList()
        }

    }

    fun checkNegativeReview(str: String) {
        var tempList = negativeList.value!!.toMutableList()

        if (negativeList.value!!.contains(str)) {
            tempList.remove(str)
            negativeList.value = tempList.toList()
        } else {
            tempList.add(str)
            negativeList.value = tempList.toList()
        }
    }

    fun resetNegativeCheckBox() {
        negativeList.value = listOf()
        binding.sendReviewCbNegative1.isChecked = false
        binding.sendReviewCbNegative2.isChecked = false
        binding.sendReviewCbNegative3.isChecked = false
        binding.sendReviewCbNegative4.isChecked = false
    }

    fun resetPositiveCheckBox() {
        positiveList.value = listOf()
        binding.sendReviewCbPositive1.isChecked = false
        binding.sendReviewCbPositive2.isChecked = false
        binding.sendReviewCbPositive3.isChecked = false
        binding.sendReviewCbPositive4.isChecked = false
    }


}
