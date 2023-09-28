package com.ias.gsscore.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.ias.gsscore.databinding.FragmentMentorsEventsBinding
import com.ias.gsscore.ui.adapter.*

class MentorsEventsFragmentViewModel(binding: ViewDataBinding) : ViewModel() {
    var binding = binding as FragmentMentorsEventsBinding
    var mentorEventListAdapter: MentorsEventsListAdapter = MentorsEventsListAdapter()
//    @SuppressLint("StaticFieldLeak")
    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context

    fun getChatData() {
        var iconName =
            listOf(
                "Essay Writing Session by Manoj K Jha",
                "Essay Writing Session by Manoj K Jha",
                "Essay Writing Session by Manoj K Jha",
                "Essay Writing Session by Manoj K Jha",
                "Essay Writing Session by Manoj K Jha",
                "Essay Writing Session by Manoj K Jha",
            )
            mentorEventListAdapter.updateList(iconName)

    }
}