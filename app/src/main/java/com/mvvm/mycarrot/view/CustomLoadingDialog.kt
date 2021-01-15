package com.mvvm.mycarrot.view

import android.app.Activity
import android.graphics.drawable.AnimationDrawable
import android.graphics.drawable.ColorDrawable
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatDialog
import com.mvvm.mycarrot.R


class CustomLoadingDialog(private val activity: Activity) {

    lateinit var progressDialog: AppCompatDialog


    fun show() {
        progressDialog = AppCompatDialog(activity)
        progressDialog.setCancelable(false)
        progressDialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.WHITE))
        progressDialog.setContentView(R.layout.progress_loading_login)
        var matrics = activity.resources.displayMetrics
        var screenwidth = matrics.widthPixels*1
        progressDialog.window!!.setLayout(screenwidth,ViewGroup.LayoutParams.MATCH_PARENT)
        progressDialog.show()


        val loadingFrame = progressDialog.findViewById<ImageView>(R.id.progressLoadingLogin_iv)!!
        val frameAnimation = loadingFrame.background as AnimationDrawable
        loadingFrame.post { frameAnimation.start() }
    }

    fun dismiss() {
        if (this::progressDialog.isInitialized) {
            if (progressDialog != null && progressDialog.isShowing) {
                progressDialog.dismiss()
            }
        }
    }


}