package com.mvvm.mycarrot.presentation.my

import android.graphics.Color
import android.os.Bundle
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.mvvm.mycarrot.R
import com.mvvm.mycarrot.databinding.ActivityNoticeBinding

class NoticeActivity : AppCompatActivity() {

    private lateinit var binding: ActivityNoticeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var mode = intent.getStringExtra("mode")!!
        initBinding()
        initStatusBar()
        initWebView(mode)

    }

    override fun onBackPressed() {
        if (binding.noticeWb.canGoBack()) binding.noticeWb.goBack()
        else super.onBackPressed()
    }

    private fun initWebView(mode: String) {

        val url =
            if (mode == "notice") "https://www.daangn.com/wv/notices/" else "https://www.daangn.com/wv/faqs"

        binding.noticeTvTitle.text = if(mode=="notice") "공지사항" else "고객센터"
        binding.noticeWb.webViewClient = WebViewClient()
        binding.noticeWb.settings.apply {
            javaScriptEnabled = true
            setSupportMultipleWindows(false)
            javaScriptCanOpenWindowsAutomatically = false
            loadWithOverviewMode = true
            useWideViewPort = true
            setSupportZoom(false)
            builtInZoomControls = false
            layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
            cacheMode = WebSettings.LOAD_NO_CACHE
            domStorageEnabled = true
        }
        binding.noticeWb.loadUrl(url)

    }

    private fun initBinding() {
        binding = DataBindingUtil.setContentView(this, R.layout.activity_notice)
        binding.apply {
            av = this@NoticeActivity
            lifecycleOwner = this@NoticeActivity
        }
    }

    private fun initStatusBar() {
        window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
        window.statusBarColor = Color.TRANSPARENT
    }
}