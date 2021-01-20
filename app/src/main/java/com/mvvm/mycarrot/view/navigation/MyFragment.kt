package com.mvvm.mycarrot.view.navigation

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.FragmentMyBinding
import com.mvvm.mycarrot.view.*
import com.mvvm.mycarrot.view.sellList.SellListActivity
import com.mvvm.mycarrot.viewModel.MyViewModel

class MyFragment : Fragment() {


    lateinit var binding: FragmentMyBinding
    lateinit var myViewModel: MyViewModel
    lateinit var customDialog: CustomProgressDialog

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        customDialog = CustomProgressDialog(activity!!)

        initViewModel()
        initBinding()

    }

    private fun initViewModel() {
        myViewModel =
            ViewModelProvider(activity!!, MyViewModel.Factory(activity!!.application)).get(
                MyViewModel::class.java
            )

        myViewModel.getIsStartItemActivity().observe(this, Observer { isStartActivity ->
            if (isStartActivity == 1 && myViewModel.getselectedFragment() == "myFm") {
                startProfileActivity()
            }
        })
    }

    private fun initBinding() {
        binding.apply {
            lifecycleOwner = this@MyFragment
            fm = this@MyFragment
            vm = myViewModel
        }
    }


    fun beforeStartProfileActivity() {
        customDialog.show()
        myViewModel.setselectedItemOwner(
            myViewModel.getCurrentUserObject().value!!.userId!!,
            "myFm"
        )
    }


    fun startNeighborhoodCertificationActivity() {
        startActivity(Intent(activity, NeighborhoodCertificationActivity::class.java))
    }

    fun startCollectActivity() {
        startActivity(Intent(activity, CollectActivity::class.java))
    }

    fun startProfileActivity() {
        customDialog.dismiss()
        myViewModel.clearIsStartItemActivity()
        startActivity(Intent(activity, ProfileActivity::class.java))
    }

    fun startSetupTownActivity() {
        startActivity(Intent(activity, SetupTownActivity::class.java))
    }

    fun startShareActivity() {
        var intent = Intent(Intent.ACTION_SEND)
        intent.addCategory(Intent.CATEGORY_DEFAULT)
        intent.putExtra(Intent.EXTRA_TEXT, getString(R.string.share))
        intent.putExtra(Intent.EXTRA_TITLE, "당근마켓")
        intent.setType("text/plain")
        startActivity(Intent.createChooser(intent, "앱을 선택해주세용"))
    }

    fun startEditProfileActivity() {
        startActivity(Intent(activity, EditProfileActivity::class.java))
    }

    fun startSellListActivity() {
        startActivity(Intent(activity, SellListActivity::class.java))
    }

    fun startLikeListActivity() {
        startActivity(Intent(activity, LikeListActivity::class.java))
    }

    fun startBuyListActivity() {
        startActivity(Intent(activity, BuyListActivity::class.java))
    }

    fun startNoticeActivity() {
        var intent = Intent(activity, NoticeActivity::class.java)
        intent.putExtra("mode", "notice")
        startActivity(intent)
    }

    fun startSupportActivity() {
        var intent = Intent(activity, NoticeActivity::class.java)
        intent.putExtra("mode", "support")
        startActivity(intent)
    }


}
