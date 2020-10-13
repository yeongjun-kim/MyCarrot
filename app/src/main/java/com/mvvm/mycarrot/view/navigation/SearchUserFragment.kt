package com.mvvm.mycarrot.view.navigation

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.adapter.UserRvAdapter
import com.mvvm.mycarrot.databinding.FragmentSearchUserBinding
import com.mvvm.mycarrot.viewModel.SearchViewModel

class SearchUserFragment : Fragment() {

    lateinit var binding: FragmentSearchUserBinding
    lateinit var searchViewModel: SearchViewModel
    var userRvAdapter = UserRvAdapter()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_search_user, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)



        searchViewModel =
            ViewModelProvider(activity!!, SearchViewModel.Factory(activity!!.application)).get(
                SearchViewModel::class.java
            )


        searchViewModel.getKeywordUserList().observe(this, Observer { itemList ->
            itemList.forEachIndexed { index, userObject ->
                Log.d("fhrm", "SearchUserFragment -onActivityCreated(),    index: ${index}, user: ${userObject.nickname}")
            }
            userRvAdapter.setList(itemList)
        })

        initUserRv()

    }

    private fun initUserRv() {
        binding.fmSearchUserRv.run {
            setHasFixedSize(true)
            layoutManager = LinearLayoutManager(activity)
            adapter = userRvAdapter

            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                    super.onScrolled(recyclerView, dx, dy)
                    val lastPosition =
                        (recyclerView.layoutManager as LinearLayoutManager).findLastCompletelyVisibleItemPosition()
                    val totalCount = recyclerView.adapter!!.itemCount

                    if (lastPosition == totalCount - 1) {
                        searchViewModel.setKeywordUserList()
                    }
                }
            })
        }

        userRvAdapter.listener = object : UserRvAdapter.ClickListener {
            override fun onClick(position: Int) {
                Log.d("fhrm", "SearchUserFragment -onClick(),    click position: ${position}")
            }

        }
    }

    /*
    SearchFragmnet에서 Keyword 입력 후 Enter 시 검색 결과 Rv Show, "toSearchRv"
    [취소] 버튼 클릭 시 일반 화면 Show, "toDefault"
    */
    fun changeLayout(mode: String) {
        if(!this::binding.isInitialized) return
        if (mode == "toSearchRv") {
            binding.fmSearchUserClDefault.visibility = View.GONE
            binding.fmSearchUserClSearch.visibility = View.VISIBLE
        } else if (mode == "toDefault") {
            binding.fmSearchUserClDefault.visibility = View.VISIBLE
            binding.fmSearchUserClSearch.visibility = View.GONE
        }
    }


    fun test(){
        Log.d("fhrm", "SearchUserFragment -test(),    here")
    }
}
