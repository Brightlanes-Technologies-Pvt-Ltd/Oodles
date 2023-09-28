package com.ias.gsscore.utils

import android.annotation.SuppressLint
import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.graphics.drawable.LayerDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.util.Log
import android.view.View
import android.webkit.WebView
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import androidx.work.*
import com.ias.gsscore.R
import com.ias.gsscore.downloadfileswithworkmanager.*
import com.ias.gsscore.downloadfileswithworkmanager.FileDownloadWorker.Companion.WORK_IN_PROGRESS
import com.ias.gsscore.downloadfileswithworkmanager.FileDownloadWorker.Companion.WORK_PROGRESS_VALUE
import com.ias.gsscore.downloadfileswithworkmanager.FileDownloadWorker.Companion.WORK_TYPE
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.File
import java.util.UUID


class Helpers {

    companion object {

        var dialog: AlertDialog ? = null
        fun downloadPdf(context: Context, url: String?, title: String?) {
            context.startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(url)))

            /*  val format = "https://drive.google.com/viewerng/viewer?embedded=true&url=%s"
            val fullPath: String = java.lang.String.format(Locale.ENGLISH, format, Uri.parse(url))
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse(fullPath))
            context.startActivity(browserIntent)*/
        }

        fun downloadPdfOld(context: Context, url: String?, title: String?): Long {
            if (url == null || url == "") {
                Toast.makeText(context, "PDF not found!", Toast.LENGTH_SHORT).show()
                return 0
            }
            val direct = File(Environment.getExternalStoragePublicDirectory("Download").toString())

            if (!direct.exists()) {
                direct.mkdirs()
            }
            val extension = url?.substring(url.lastIndexOf("."))
            val downloadReference: Long
            var dm: DownloadManager =
                context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val uri = Uri.parse(url)
            val request = DownloadManager.Request(uri)
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_DOWNLOADS,
                title + "pdf" + System.currentTimeMillis() + extension
            )
            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
            request.setTitle(title)
            Toast.makeText(context, "Downloading..", Toast.LENGTH_SHORT).show()

            downloadReference = dm?.enqueue(request) ?: 0

            return downloadReference

        }

        @SuppressLint("SetJavaScriptEnabled")
        fun setWebViewText(web_view: WebView, url: String) {
            web_view.requestFocus()
            web_view.settings.lightTouchEnabled = true
            web_view.settings.javaScriptEnabled = true
            web_view.run { settings.setGeolocationEnabled(true) }
            web_view.isSoundEffectsEnabled = true
            web_view.loadData(url, "text/html", "UTF-8")
        }


        fun startDownloadingFile(
            rootView: FrameLayout?,
            context: Context,
            url: String,
            fileName: String,
            type: String,
            workManager: WorkManager,
            lifecycleOwner: LifecycleOwner
        ) {
            val progressBar = ProgressBar(context, null, android.R.attr.progressBarStyleHorizontal)
            showProgressBar(progressBar, context)
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresStorageNotLow(true)
                .setRequiresBatteryNotLow(true)
                .build()
            val data = Data.Builder()

            data.apply {
                putString(KEY_FILE_NAME, fileName)
                putString(KEY_FILE_URL, url)
                putString(KEY_FILE_TYPE, type)
            }

            val oneTimeWorkRequest = OneTimeWorkRequest.Builder(FileDownloadWorker::class.java)
                .setConstraints(constraints)
                .setInputData(data.build())
                .build()
//            Toast.makeText(context, "Downloading..", Toast.LENGTH_SHORT).show()

            workManager.enqueue(oneTimeWorkRequest)
           // observeWorkProgress(oneTimeWorkRequest.id, workManager, progressBar, lifecycleOwner)
            workManager.getWorkInfoByIdLiveData(oneTimeWorkRequest.id)
                .observe(lifecycleOwner) { workInfo ->
                    if (workInfo != null) {
                        when (workInfo.state) {
                            WorkInfo.State.SUCCEEDED -> {
                                dialog?.dismiss()
                                progressBar.progress = 0
                                Log.d("Download progress to progressbar ======", "success")
                            }
                            WorkInfo.State.FAILED, WorkInfo.State.CANCELLED, WorkInfo.State.BLOCKED -> {
                                Log.d("Download progress to progressbar ======", "cancel")
                                progressBar.progress = 0
                                progressBar.visibility = View.GONE
                            }
                            else -> {
                                if(workInfo.progress.getString(WORK_TYPE) == WORK_IN_PROGRESS){
                                    val progress = workInfo.progress.getInt(WORK_PROGRESS_VALUE, 0)
                                    progressBar.visibility = View.VISIBLE

                                    progressBar.progress = progress
                                }
                            }
                        }
                    }
                    /*info?.let {
                        when (it.state) {
                            WorkInfo.State.SUCCEEDED -> {
                                val uri = it.outputData.getString(KEY_FILE_URI)
                                uri?.let {
                                    Log.d("progress===","Download SUCCEEDED")
                                }
                            }

                            WorkInfo.State.FAILED -> {
                                Log.d("progress===","Download Failed")
                                dialog?.dismiss()
                            }

                            WorkInfo.State.RUNNING -> {
                                Log.d("progress===","Download RUNNING")
                            }

                            else -> {

                            }
                        }
                    }*/
                }
        }

        fun isNetworkAvailable(context: Context): Boolean {
            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                val nw = connectivityManager.activeNetwork ?: return false
                val actNw = connectivityManager.getNetworkCapabilities(nw) ?: return false
                return when {
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                    //for other device how are able to connect with Ethernet
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                    //for check internet over Bluetooth
                    actNw.hasTransport(NetworkCapabilities.TRANSPORT_BLUETOOTH) -> true
                    else -> false
                }
            } else {
                return connectivityManager.activeNetworkInfo?.isConnected ?: false
            }
        }

        private fun showProgressBar(progressBar: ProgressBar, context: Context) {
            progressBar.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            progressBar.setPadding(5, 0, 5, 0)
            progressBar.max = 100
            progressBar.progress = 0
            val progressColor = ContextCompat.getColor(context, R.color.blue_text) // Replace with your desired color resource
            // Get the progressDrawable of the ProgressBar
            val layers = progressBar.progressDrawable as LayerDrawable
            // Set the color of the progress layer
            val progressDrawable = layers.getDrawable(1) as Drawable
            progressDrawable.setColorFilter(progressColor, PorterDuff.Mode.SRC_IN)
            // Set the modified progressDrawable back to the ProgressBar
            progressBar.progressDrawable = layers
            val builder = AlertDialog.Builder(context)
            builder.setMessage("Downloading...")
            builder.setView(progressBar)
            dialog = builder.create()
            dialog?.setCancelable(false)
            dialog?.show()
        }
    }

}