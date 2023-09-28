package com.ias.gsscore.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.ias.gsscore.databinding.ActivityFeedbackFormBinding

class FeedbackFormViewModel(binding: ViewDataBinding) : ViewModel() {
    var binding = binding as ActivityFeedbackFormBinding

    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context

    fun clickBack() {
        (context as Activity).finish()
    }

    fun clickQuestionPdf() {
        Toast.makeText(
            context,
            "Question PDF",
            Toast.LENGTH_SHORT
        )
            .show()
    }

}