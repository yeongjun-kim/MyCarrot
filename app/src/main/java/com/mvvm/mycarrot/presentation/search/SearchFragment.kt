package com.mvvm.mycarrot.presentation.search

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.google.android.material.tabs.TabLayoutMediator
import com.mvvm.mycarrot.databinding.FragmentSearchBinding


class SearchFragment : Fragment() {

    lateinit var binding: FragmentSearchBinding
    lateinit var searchViewModel: SearchViewModel
    var searchTradingFragment =
        SearchTradingFragment()
    var searchUserFragment =
        SearchUserFragment()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater,
            com.mvvm.mycarrot.R.layout.fragment_search,
            container,
            false
        )
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        initViewModel()
        initBingding()
        initTabLayoutViewPager()
        initEditTextListener()


    }

    private fun initBingding() {
        binding.apply {
            fm = this@SearchFragment
            lifecycleOwner = this@SearchFragment
        }
    }

    private fun initViewModel() {
        searchViewModel =
            ViewModelProvider(activity!!, SearchViewModel.Factory(activity!!.application)).get(
                SearchViewModel::class.java
            )
    }

    fun cancel() {
        binding.searchEt.text.clear()
        searchTradingFragment.changeLayout("toNestedScrollView")
        searchUserFragment.changeLayout("toDefault")
        searchViewModel.clearKeywordList()
    }

    private fun initEditTextListener() {
        binding.searchEt.imeOptions = EditorInfo.IME_ACTION_SEARCH
        binding.searchEt.setOnEditorActionListener { _, _, _ ->
            hideSoftKeyboard()
            if (binding.searchEt.text.isNullOrBlank()) {
                Toast.makeText(activity, "검색어를 입력해주세용", Toast.LENGTH_SHORT).show()
            } else {
                searchTradingFragment.changeLayout("toSearchRv")
                searchUserFragment.changeLayout("toSearchRv")
                searchViewModel.setKeyword(binding.searchEt.text.toString())
                searchViewModel.test()
            }
            true
        }
    }

    private fun hideSoftKeyboard() {
        val imm = context!!.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(binding.searchEt.windowToken, 0)
    }


    private fun initTabLayoutViewPager() {
        val vpAdapter = ViewPagerAdapter(activity!!)
        binding.searchVp.adapter = vpAdapter

        TabLayoutMediator(binding.searchTl, binding.searchVp) { tab, position ->
            val tabLayoutTextArray = arrayOf("중고거래", "사람")
            tab.text = tabLayoutTextArray[position]
        }.attach()
    }

    private inner class ViewPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

        override fun createFragment(position: Int): Fragment {
            return when (position) {
                0 -> searchTradingFragment
                1 -> searchUserFragment
                else -> searchTradingFragment
            }
        }

        override fun getItemCount(): Int = 2
    }
}
