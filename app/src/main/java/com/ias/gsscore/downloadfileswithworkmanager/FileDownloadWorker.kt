package com.ias.gsscore.downloadfileswithworkmanager

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.ContentValues
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.ias.gsscore.R
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import java.io.BufferedInputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.net.URL


class FileDownloadWorker(
    private val context: Context,
    private val workerParameters: WorkerParameters
) : CoroutineWorker(context, workerParameters) {

    companion object {
        const val WORK_TYPE = "WORK_TYPE"
        const val WORK_IN_PROGRESS = "WORK_IN_PROGRESS"
        const val WORK_PROGRESS_VALUE = "WORK_PROGRESS_VALUE"
    }

    private val notificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

    @RequiresApi(Build.VERSION_CODES.O)
    override suspend fun doWork(): Result {
        val mimeType = when (workerParameters.inputData.getString(KEY_FILE_TYPE)) {
            "PDF" -> "application/pdf"
            "PNG" -> "image/png"
            "MP4" -> "video/mp4"
            else -> ""
        }
        val filename = workerParameters.inputData.getString(KEY_FILE_NAME)
        val url = workerParameters.inputData.getString(KEY_FILE_URL)
        val downloadedFilePath = downloadFileFromURL(url, filename)
        return if (downloadedFilePath.isNotEmpty()) {
            Result.success(workDataOf(KEY_FILE_URI to downloadedFilePath))
        } else {
            Result.failure()
        }
        return Result.failure()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayNotification(uri: String) {
        val channel = NotificationChannel(
            CHANNEL_NAME,
            CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.enableVibration(false)
        notificationManager.createNotificationChannel(channel)

        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri.toUri(), "application/pdf")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val contentIntent = PendingIntent.getActivity(
            context,
            NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, CHANNEL_NAME)


        notificationBuilder
            .setContentTitle(CHANNEL_DESC)
            .setSmallIcon(R.drawable.ic_dowload_black)
            .setContentIntent(contentIntent)
        notificationManager.notify(NOTIFICATION_ID, notificationBuilder.build())
    }


    private fun downloadFileFromUri(url: String, mimeType: String, filename: String?): Uri? {
        Log.d("progress=== url ", url)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {

            val contentValues = ContentValues().apply {
                put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                put(MediaStore.MediaColumns.MIME_TYPE, mimeType)
                put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_DOWNLOADS)
            }

            val resolver = context.contentResolver
            val uri = resolver.insert(MediaStore.Downloads.EXTERNAL_CONTENT_URI, contentValues)
            return if (uri != null) {
                URL(url).openStream().use { input ->
                    resolver.openOutputStream(uri).use { output ->
                        input.copyTo(output!!, DEFAULT_BUFFER_SIZE)
                    }
                }
                uri
            } else {
                null
            }

        } else {
            val target = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename)
            URL(url).openStream().use { input ->
                FileOutputStream(target).use { output ->
                    input.copyTo(output)
                }
            }
            return target.toUri()
        }
    }

    /**
     * Download the file from the URL
     * @param imageUrl
     * @param filename
     */
      private suspend fun downloadFileFromURL(imageUrl: String?,  filename: String?): String {
        var dowloadedImageURl = ""
        if(!imageUrl.isNullOrEmpty() && !filename.isNullOrEmpty()) {
            val destFile = File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS), filename)
            createFileIfNotExist(destFile)
//            withContext(Dispatchers.IO) {
                val url = URL(imageUrl)
                val connection = url.openConnection()
                connection.connect()
                val lengthOfFile = connection.contentLength
                // download the file
                val input = BufferedInputStream(connection.getInputStream())
                // Output stream
                val output = FileOutputStream(destFile)
                val data = ByteArray(1024)
                var total = 0
                var last = 0

                while (true) {
                    val count = input.read(data)
                    if (count == -1) break
                    total += count
                    val progress = (total * 100 / (1024 * 1024)).coerceIn(0, 100)
                    if (progress % 10 == 0) {
                        if (last != progress) {
                            Log.d("Download progress ======", progress.toString())
                            setProgress(
                                workDataOf(
                                    WORK_TYPE to WORK_IN_PROGRESS,
                                    WORK_PROGRESS_VALUE to progress
                                )
                            )
                        }
                        last = progress
                    }
                    output.write(data, 0, count)
                }
                output.flush()
                output.close()
                input.close()
                dowloadedImageURl = destFile.path
            }
//        }
        return dowloadedImageURl
    }

    private fun createFileIfNotExist(file : File){
        try {
            if (!file.exists()) {
                file.createNewFile()
                println("File created: ${file.name}")
            } else {
                println("File already exists: ${file.name}")
            }
        } catch (e: IOException) {
            println("An error occurred: ${e.message}")
        }
    }
}

