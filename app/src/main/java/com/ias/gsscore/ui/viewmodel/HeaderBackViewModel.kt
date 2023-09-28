package com.ias.gsscore.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel

class HeaderBackViewModel(binding: ViewDataBinding) : ViewModel() {
    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context

    fun clickBackPress(){
        (context as Activity).onBackPressed()
    }
}