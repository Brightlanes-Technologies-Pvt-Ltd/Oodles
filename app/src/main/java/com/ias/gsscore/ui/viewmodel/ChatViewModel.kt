package com.ias.gsscore.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.ias.gsscore.databinding.ActivityChatBinding

class ChatViewModel(binding: ViewDataBinding) : ViewModel() {
    var binding = binding as ActivityChatBinding
    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context
}