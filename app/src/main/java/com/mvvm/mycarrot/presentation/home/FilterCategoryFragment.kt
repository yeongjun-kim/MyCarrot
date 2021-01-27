package com.mvvm.mycarrot.presentation.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.FragmentFilterCategoryBinding
import com.mvvm.mycarrot.presentation.common.MainActivity
import kotlinx.android.synthetic.main.fragment_filter_category.*

class FilterCategoryFragment : Fragment() {

    lateinit var homeViewModel: HomeViewModel
    lateinit var binding: FragmentFilterCategoryBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_filter_category, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        initViewModel()
        initBinding()
        initCheck()
    }

    private fun initBinding() {
        binding.apply {
            vm = homeViewModel
            fm = this@FilterCategoryFragment
            lifecycleOwner = this@FilterCategoryFragment
        }
    }

    private fun initViewModel() {
        homeViewModel = ViewModelProvider(
            activity!!, HomeViewModel.Factory(activity!!.application)
        ).get(HomeViewModel::class.java)
    }

    fun onBackPress() {
        activity!!.supportFragmentManager.beginTransaction()
            .setCustomAnimations(android.R.animator.fade_in, android.R.animator.fade_out)
            .addToBackStack(null)
            .replace(R.id.main_fl, (activity as MainActivity).homeFragment).commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeViewModel.isFromCategoryFragment = true
    }

    private fun initCheck() {
        if (homeViewModel.categoryList.contains("디지털/가전")) {
            binding.filterCategoryCbDigital.isChecked = true
        }
        if (homeViewModel.categoryList.contains("가구/인테리어")) {
            binding.filterCategoryCbFurniture.isChecked = true
        }
        if (homeViewModel.categoryList.contains("생활/가공식품")) {
            binding.filterCategoryCbLife.isChecked = true
        }
        if (homeViewModel.categoryList.contains("여성패션/잡화")) {
            binding.filterCategoryCbWoman.isChecked = true
        }
        if (homeViewModel.categoryList.contains("남성패션/잡화")) {
            binding.filterCategoryCbMan.isChecked = true
        }
        if (homeViewModel.categoryList.contains("게임/취미/스포츠/레저")) {
            binding.filterCategoryCbGame.isChecked = true
        }
        if (homeViewModel.categoryList.contains("뷰티/미용")) {
            binding.filterCategoryCbBeauty.isChecked = true
        }
        if (homeViewModel.categoryList.contains("유아/반려동물용품")) {
            binding.filterCategoryCbPet.isChecked = true
        }
        if (homeViewModel.categoryList.contains("도서/티켓/음반")) {
            binding.filterCategoryCbBook.isChecked = true
        }
        if (homeViewModel.categoryList.contains("삽니다")) {
            binding.filterCategoryCbBuy.isChecked = true
        }

    }
}

