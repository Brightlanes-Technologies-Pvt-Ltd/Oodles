package com.ias.gsscore.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.ias.gsscore.databinding.ActivityPersonalProfileBinding

class PersonalProfileViewModel(binding: ViewDataBinding) : ViewModel() {
    var binding = binding as ActivityPersonalProfileBinding
    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context
}