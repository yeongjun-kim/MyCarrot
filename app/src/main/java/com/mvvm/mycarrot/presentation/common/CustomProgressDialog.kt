package com.mvvm.mycarrot.presentation.common

import android.app.Activity
import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatDialog
import android.graphics.drawable.AnimationDrawable
import android.widget.ImageView
import com.mvvm.mycarrot.R


class CustomProgressDialog(private val activity: Activity) {

    lateinit var progressDialog: AppCompatDialog


    fun show() {
        progressDialog = AppCompatDialog(activity)
        progressDialog.setCancelable(false)
        progressDialog.window!!.setBackgroundDrawable(ColorDrawable(android.graphics.Color.TRANSPARENT))
        progressDialog.setContentView(R.layout.progress_loading)
        progressDialog.show()


        val loadingFrame = progressDialog.findViewById<ImageView>(R.id.progressLoading_iv)!!
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