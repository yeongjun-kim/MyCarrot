package com.mvvm.mycarrot.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mvvm.mycarrot.R
import kotlinx.android.synthetic.main.fm3.*

class fm3 : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fm3, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)


        test_fm3_btn.setOnClickListener {
            activity!!.finish()
        }

        test_fm3_backpress.setOnClickListener {
            fragmentManager!!.popBackStack()
        }

    }
}
