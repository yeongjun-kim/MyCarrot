package com.mvvm.mycarrot.view.navigation

import android.content.Intent
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
import com.mvvm.mycarrot.view.CustomProgressDialog
import com.mvvm.mycarrot.view.ProfileActivity
import com.mvvm.mycarrot.viewModel.HomeViewModel
import com.mvvm.mycarrot.viewModel.SearchViewModel

class SearchUserFragment : Fragment() {

    lateinit var binding: FragmentSearchUserBinding
    lateinit var searchViewModel: SearchViewModel
    lateinit var homeViewModel: HomeViewModel // Item Click 에 대한 로직이 있어서 Click에 사용 (selectedItem)
    lateinit var customDialog: CustomProgressDialog

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

        customDialog = CustomProgressDialog(activity!!)

        binding.apply {
            lifecycleOwner = this@SearchUserFragment
        }


        searchViewModel =
            ViewModelProvider(activity!!, SearchViewModel.Factory(activity!!.application)).get(
                SearchViewModel::class.java
            )

        homeViewModel =
            ViewModelProvider(this, HomeViewModel.Factory(activity!!.application)).get(
                HomeViewModel::class.java
            )


        searchViewModel.getKeywordUserList().observe(this, Observer { itemList ->
            userRvAdapter.setList(itemList)
        })


        homeViewModel.getIsStartItemActivity().observe(this, Observer {isStartActivity ->
            if(isStartActivity == 1 && homeViewModel.getselectedFragment() == "searchUserFm"){
                startProfileActivity()
            }

        })


        initFirstLayout()
        initUserRv()

    }

    /*
     SearchTradingFragment 화면에서 검색 후 해당 fragment 로 오면 검색 Rv 가 아닌 default layout 이 show 되기때문에,
     해당 fragment 가 show 될때 keyword 를 검색 했는지 여부 확인하여 보여질 layout 을 보여줌
     */
    private fun initFirstLayout() {
        if (searchViewModel.getKeyword().isNullOrBlank()) changeLayout("toDefault")
        else changeLayout("toSearchRv")

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
                        Log.d("fhrm", "SearchUserFragment -onScrolled(),    : here")
                        searchViewModel.setKeywordUserList()
                    }
                }
            })
        }

        userRvAdapter.listener = object : UserRvAdapter.ClickListener {
            override fun onClick(position: Int) {
                beforeStartProfileActivity(position)
            }

        }
    }

    /*
    SearchFragmnet에서 Keyword 입력 후 Enter 시 검색 결과 Rv Show, "toSearchRv"
    [취소] 버튼 클릭 시 일반 화면 Show, "toDefault"
    */
    fun changeLayout(mode: String) {
        if (!this::binding.isInitialized) return
        if (mode == "toSearchRv") {
            binding.fmSearchUserClDefault.visibility = View.GONE
            binding.fmSearchUserClSearch.visibility = View.VISIBLE
        } else if (mode == "toDefault") {
            binding.fmSearchUserClDefault.visibility = View.VISIBLE
            binding.fmSearchUserClSearch.visibility = View.GONE
        }
    }

    fun beforeStartProfileActivity(position:Int){
        customDialog.show()
        homeViewModel.setselectedItemOwner(userRvAdapter.itemList[position].userId!!, "searchUserFm")
    }

    fun startProfileActivity(){
        customDialog.dismiss()
        homeViewModel.clearIsStartItemActivity()
        startActivity(Intent(activity, ProfileActivity::class.java))
    }

}
