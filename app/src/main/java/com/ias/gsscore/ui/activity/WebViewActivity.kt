package com.ias.gsscore.ui.activity

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Bundle
import android.os.Environment
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.ias.gsscore.R
import com.ias.gsscore.databinding.ActivityWebViewBinding
import com.ias.gsscore.network.RetrofitHelper
import java.io.File

class WebViewActivity : AppCompatActivity() {
    private lateinit var binding : ActivityWebViewBinding
    private lateinit var loadingDialog: AlertDialog

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_web_view)
        setContentView(binding.root)

        loadingDialog = RetrofitHelper.loadingDialog(this)
        val title = intent.getStringExtra("title")
        val url = intent.getStringExtra("url") ?: "https://docs.google.com/gview?embedded=true&url"
        binding.tvTitle.text = title
   //     downloadPdf(this,url,title)
        binding.ivBack.setOnClickListener { finish() }
        binding.webView.clearCache(true);
        binding.webView.clearFormData();
        binding.webView.clearHistory();
        binding.webView.clearSslPreferences();
        binding.webView.webViewClient = WebViewClient()
        binding.webView.settings.setSupportZoom(true)
        binding.webView.settings.javaScriptEnabled = true
        binding.webView.settings.loadWithOverviewMode = true
        binding.webView.settings.useWideViewPort = true
        binding.webView.settings.domStorageEnabled = true
   //     binding.webView.loadUrl("https://docs.google.com/gview?embedded=true&url=$url")
        binding.webView.loadUrl(url)
        setWebClient()
        onPageFinished(binding.webView,url)
    }

    private fun onPageFinished(view: WebView, url: String?) {
        if (view.title.equals(""))
            view.reload()
    }

    private fun setWebClient() {
        loadingDialog.show()
        binding.webView.webChromeClient = object : WebChromeClient() {
            override fun onProgressChanged(
                view: WebView,
                newProgress: Int
            ) {
                super.onProgressChanged(view, newProgress)
              /* // binding.progressBar.progress = newProgress
                if (newProgress < 100 && binding.progressBar.visibility == ProgressBar.GONE) {
                  //  binding.progressBar.visibility = ProgressBar.VISIBLE
                }*/
                if (newProgress == 100) {
                    loadingDialog.dismiss()
                  //  binding.progressBar.visibility = ProgressBar.GONE
                }
            }
        }
    }

    fun downloadPdf(context: Context, url: String?, title: String?): Long {
        val direct = File(Environment.getExternalStorageDirectory().toString())

        if (!direct.exists()) {
            direct.mkdirs()
        }
        val extension = url?.substring(url.lastIndexOf("."))
        val downloadReference: Long
        var  dm: DownloadManager
        dm= context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        val uri = Uri.parse(url)
        val request = DownloadManager.Request(uri)
        request.setDestinationInExternalPublicDir(
            Environment.DIRECTORY_DOWNLOADS,
            title+"pdf" + System.currentTimeMillis() + extension
        )
        request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
        request.setTitle(title)
        Toast.makeText(context, "Downloading..", Toast.LENGTH_SHORT).show()

        downloadReference = dm?.enqueue(request) ?: 0

        return downloadReference

    }
}