package com.ias.gsscore.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.webkit.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ias.gsscore.R
import com.ias.gsscore.databinding.ActivityWebViewVideoBinding
import com.ias.gsscore.network.RetrofitHelper


class VideoWebViewActivity : AppCompatActivity() {
    lateinit var binding : ActivityWebViewVideoBinding
    lateinit var loadingDialog: AlertDialog
    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_web_view_video)
        setContentView(binding.root)
        loadingDialog = RetrofitHelper.loadingDialog(this)
        val title = intent.getStringExtra("title")
        val url = intent.getStringExtra("url")
        binding.tvTitle.text = title
        binding.ivBack.setOnClickListener { finish() }
        binding.webView.webViewClient = WebViewClient()
        binding.webView.settings.javaScriptEnabled = true
        try {
            val settings: WebSettings = binding.webView.settings
            settings.javaScriptCanOpenWindowsAutomatically = true
            settings.setSupportMultipleWindows(true)
            settings.builtInZoomControls = true
            settings.javaScriptEnabled = true
            settings.databaseEnabled = true
            settings.domStorageEnabled = true
            settings.setSupportZoom(true)
            settings.builtInZoomControls = true
            settings.setSupportZoom(true);

        } catch (e: Exception) {
        }
        Log.d("****** VideoWebViewActivity >>", ""+url)
        binding.webView.loadUrl(url!!)

    }




}