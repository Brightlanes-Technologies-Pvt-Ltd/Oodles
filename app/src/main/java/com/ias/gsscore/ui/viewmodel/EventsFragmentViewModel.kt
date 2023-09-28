package com.ias.gsscore.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.ias.gsscore.databinding.FragmentEventsBinding
import com.ias.gsscore.ui.adapter.EventsListAdapter

class EventsFragmentViewModel(binding: ViewDataBinding) : ViewModel() {
    var binding = binding as FragmentEventsBinding
    var chatListAdapter: EventsListAdapter = EventsListAdapter()
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
        chatListAdapter.updateList(iconName)
    }
}