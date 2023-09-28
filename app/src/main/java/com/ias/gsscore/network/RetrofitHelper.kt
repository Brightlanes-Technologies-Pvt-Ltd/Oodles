package com.ias.gsscore.network

import android.app.Activity
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import com.ias.gsscore.R
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitHelper {
    private const val baseUrl = "https://iasscore.in"
    public const val YOUTUBE_API_KEY = "AIzaSyCC3bC_D5uwBetXOa8qqwE-rm1ErYWX0WM"

    private val interceptor = run {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.apply {
            httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        }
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(interceptor) // same for .addInterceptor(...)
        .connectTimeout(30, TimeUnit.SECONDS) //Backend is really slow
        .writeTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
          .build()

    lateinit var retrofit: Retrofit
    fun getInstance(): Retrofit {
        return if (RetrofitHelper::retrofit.isInitialized) {
            retrofit
        } else {
            Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }
    }

    fun loadingDialog(context: Context): AlertDialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(context)
        val viewGroup: ViewGroup = (context as Activity).findViewById(android.R.id.content)
        val dialogView: View =
            LayoutInflater.from(context).inflate(R.layout.dialog_loading, viewGroup, false)
        builder.setView(dialogView)
        val alertDialog = builder.create()
        alertDialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT));
        return alertDialog
    }
}