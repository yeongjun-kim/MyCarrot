package com.mvvm.mycarrot.view.navigation

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.FragmentCategoryBinding
import com.mvvm.mycarrot.view.MainActivity
import com.mvvm.mycarrot.viewModel.HomeViewModel
import kotlinx.android.synthetic.main.fragment_category.*

class CategoryFragment : Fragment() {

    lateinit var homeViewModel: HomeViewModel
    lateinit var binding: FragmentCategoryBinding

    init {

    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_category, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        homeViewModel = ViewModelProvider(
            activity!!, HomeViewModel.Factory(activity!!.application)
        ).get(HomeViewModel::class.java)

        binding.apply {
            vm = homeViewModel
            fm = this@CategoryFragment
            lifecycleOwner = this@CategoryFragment
        }


        btn_test2.setOnClickListener {
        }

        initCheck()
    }

    fun onBackPress() {
        activity!!.supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .replace(R.id.main_fl, (activity as MainActivity).homeFragment).commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeViewModel.isFromCategoryFragment = true

    }

    private fun initCheck() {
        if (homeViewModel.categoryList.contains("디지털/가전")) {
            fm_category_cb_digital.isChecked = true
        }
        if (homeViewModel.categoryList.contains("가구/인테리어")) {
            fm_category_cb_furniture.isChecked = true
        }
        if (homeViewModel.categoryList.contains("생활/가공식품")) {
            fm_category_cb_life.isChecked = true
        }
        if (homeViewModel.categoryList.contains("여성패션/잡화")) {
            fm_category_cb_woman.isChecked = true
        }
        if (homeViewModel.categoryList.contains("남성패션/잡화")) {
            fm_category_cb_man.isChecked = true
        }
        if (homeViewModel.categoryList.contains("게임/취미/스포츠/레저")) {
            fm_category_cb_game.isChecked = true
        }
        if (homeViewModel.categoryList.contains("뷰티/미용")) {
            fm_category_cb_beauty.isChecked = true
        }
        if (homeViewModel.categoryList.contains("유아/반려동물용품")) {
            fm_category_cb_pet.isChecked = true
        }
        if (homeViewModel.categoryList.contains("도서/티켓/음반")) {
            fm_category_cb_book.isChecked = true
        }
        if (homeViewModel.categoryList.contains("삽니다")) {
            fm_category_cb_buy.isChecked = true
        }

    }
}

