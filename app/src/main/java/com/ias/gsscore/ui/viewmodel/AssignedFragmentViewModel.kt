package com.ias.gsscore.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.ias.gsscore.databinding.FragmentAssignedBinding
import com.ias.gsscore.ui.adapter.AssignedListAdapter

class AssignedFragmentViewModel(binding: ViewDataBinding) : ViewModel() {
    var binding = binding as FragmentAssignedBinding
    var chatListAdapter: AssignedListAdapter = AssignedListAdapter()
//    @SuppressLint("StaticFieldLeak")
    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context

    fun getChatData() {
        var iconName =
            listOf(
                "IAS 2022: Ethics Integrity and Aptitude + Essay Writing (Batch - 2)",
                "IAS 2022: Ethics Integrity and Aptitude + Essay Writing",
                "GS Mains Mentorship Programme 2021",
                "IAS 2022: Ethics Integrity and Aptitude + Essay Writing (Batch - 2)",
                "IAS 2022: Ethics Integrity and Aptitude + Essay Writing",
                "GS Mains Mentorship Programme 2021",
                "IAS 2022: Ethics Integrity and Aptitude + Essay Writing (Batch - 2)",
                "IAS 2022: Ethics Integrity and Aptitude + Essay Writing",
                "GS Mains Mentorship Programme 2021"
            )
        chatListAdapter.updateList(iconName)
    }
}