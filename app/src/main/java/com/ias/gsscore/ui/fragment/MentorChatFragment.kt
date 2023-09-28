package com.ias.gsscore.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.ias.gsscore.R
import com.ias.gsscore.databinding.FragmentMentorChatBinding

class MentorChatFragment() : Fragment() {
    lateinit var binding: FragmentMentorChatBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mentor_chat, container, false)
        initialData()
        return binding.root
    }

    private fun initialData() {

    }
}