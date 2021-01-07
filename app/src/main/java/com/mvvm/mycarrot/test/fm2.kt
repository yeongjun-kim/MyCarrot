package com.mvvm.mycarrot.test

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.mvvm.mycarrot.R
import kotlinx.android.synthetic.main.fm2.*

class fm2 : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fm2, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        var fm3 = fm3()

        test_fm2_backpress.setOnClickListener {
            fragmentManager!!.popBackStack()
        }
        test_fm2_btn.setOnClickListener {
            fragmentManager!!.beginTransaction()
//                .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
                .setCustomAnimations(android.R.animator.fade_in,android.R.animator.fade_out,android.R.animator.fade_in,android.R.animator.fade_out)
                .add(R.id.test_act_fl, fm3)
                .addToBackStack("fm3")
                .commit()
        }
    }
}
