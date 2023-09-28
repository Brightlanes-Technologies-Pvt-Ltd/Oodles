package com.ias.gsscore.viewmodelfactory

import android.app.Application
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ActivityViewModelFactory(val binding: ViewDataBinding, application: Application) : ViewModelProvider.AndroidViewModelFactory(application) {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return modelClass.getConstructor(ViewDataBinding::class.java).newInstance(binding)
    }
}