package com.ias.gsscore.uploadFiles

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.core.net.toUri
import androidx.work.CoroutineWorker
import androidx.work.Data
import androidx.work.ListenableWorker
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.ias.gsscore.R
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.RetrofitHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.IOException

class FileUploadWorker(
    private val context: Context,
    private val workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {
    private val notificationManager =
        applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    val TAG = "FileUploadWorker"
    var outputData: Data? = null
    @RequiresApi(Build.VERSION_CODES.O)
    /*override suspend fun doWork(): Result  {
        val mimeType = when (workerParameters.inputData.getString(FILE_TYPE)) {
            "PDF" -> "application/pdf"
            else -> ""
        }
        return try {
            val url = workerParameters.inputData.getString(FILE_URL)
            val param = HashMap<String, RequestBody>()
            param["user_id"] = workerParameters.inputData.getString(USER_ID)!!.toRequestBody()
            param["program_id"] = workerParameters.inputData.getString(PROGRAM_ID)!!.toRequestBody()
            param["test_id"] = workerParameters.inputData.getString(TEST_ID)!!.toRequestBody()
            param["user_name"] = workerParameters.inputData.getString(USER_NAME)!!.toRequestBody()
            param["roll_no"] = workerParameters.inputData.getString(ROLL_NO)!!.toRequestBody()
            uploadPDF(url, mimeType, param, context)
            delay(3000)
            Result.success(outputData!!)
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }*/

    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        return@withContext try {
            uploadImageFromUri()
        } catch (exception: Exception) {
            exception.printStackTrace()
            Result.failure()
        } catch (e: IOException) {
            e.printStackTrace()
            Result.failure()
        }
    }

    private fun uploadImageFromUri(): Result {
        val mimeType = when (workerParameters.inputData.getString(FILE_TYPE)) {
            "PDF" -> "application/pdf"
            else -> ""
        }
        val url = workerParameters.inputData.getString(FILE_URL)
        val param = HashMap<String, RequestBody>()
        param["user_id"] = workerParameters.inputData.getString(USER_ID)!!.toRequestBody()
        param["program_id"] = workerParameters.inputData.getString(PROGRAM_ID)!!.toRequestBody()
        param["test_id"] = workerParameters.inputData.getString(TEST_ID)!!.toRequestBody()
        param["user_name"] = workerParameters.inputData.getString(USER_NAME)!!.toRequestBody()
        param["roll_no"] = workerParameters.inputData.getString(ROLL_NO)!!.toRequestBody()
        uploadPDF(url, mimeType, param, context)
        return Result.success()
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun displayNotification(uri: String) {
        val channel = NotificationChannel(
            UPLOAD_CHANNEL_NAME,
            UPLOAD_CHANNEL_NAME,
            NotificationManager.IMPORTANCE_HIGH
        )
        channel.enableVibration(false)
        notificationManager.createNotificationChannel(channel)
        val intent = Intent(Intent.ACTION_VIEW)
        intent.setDataAndType(uri.toUri(), "application/pdf")
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        val contentIntent = PendingIntent.getActivity(
            context,
            UPLOAD_NOTIFICATION_ID,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notificationBuilder =
            NotificationCompat.Builder(applicationContext, UPLOAD_CHANNEL_NAME)


        notificationBuilder
            .setContentTitle(UPLOAD_CHANNEL_DESC)
            .setSmallIcon(R.drawable.ic_up)
            .setContentIntent(contentIntent)
        notificationManager.notify(UPLOAD_NOTIFICATION_ID, notificationBuilder.build())
    }


    /**
     * Upload PDF file to server
     */
    private fun uploadPDF(path: String?, mimeType : String, params : HashMap<String, RequestBody>, context: Context) {
        val retrofit = RetrofitHelper.getInstance()
        if (!path.isNullOrEmpty()) {
            val file = File(path)
            val requestFile: RequestBody = file.asRequestBody(mimeType.toMediaTypeOrNull())
            val fileToUpload: MultipartBody.Part =
                MultipartBody.Part.createFormData("answer_copy", file.name, requestFile)
            val getResponse: ApiInterface = retrofit.create(ApiInterface::class.java)
            val call: Call<HashMap<String, Any>> = getResponse.uploadPDF(fileToUpload, params)
            Log.d(TAG, "PDF Upload Called")
            call.enqueue(object : Callback<HashMap<String, Any>> {
                override fun onResponse(
                    call: Call<HashMap<String, Any>>,
                    response: Response<HashMap<String, Any>>
                ) {
                    if (response.isSuccessful) {
                        Log.d(TAG, "PDF Upload Response===" + response.body().toString())
                        val responseData: HashMap<String, Any>? = response.body()
                        if (responseData != null) {
                            Toast.makeText(
                                context,
                                responseData["message"].toString(),
                                Toast.LENGTH_SHORT
                            ).show()
                            outputData = Data.Builder().putString("file_url", responseData["message"].toString()).build()
                            Result.success(outputData!!)
                        }
                    } else if (response.errorBody() != null) {
                        Toast.makeText(context, response.errorBody().toString(), Toast.LENGTH_SHORT)
                            .show()
                        Log.d(TAG, "PDF Upload Response Error===" + response.errorBody().toString())
                    }
                }

                override fun onFailure(call: Call<HashMap<String, Any>>, t: Throwable) {
                    call.cancel()
                    Log.d(TAG, "PDF Upload Response===$call")
                }
            })
        }
    }
}

