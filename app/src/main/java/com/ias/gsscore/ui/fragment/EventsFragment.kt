package com.ias.gsscore.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ias.gsscore.R
import com.ias.gsscore.databinding.FragmentEventsBinding
import com.ias.gsscore.ui.viewmodel.EventsFragmentViewModel
import com.ias.gsscore.utils.SingletonClass
import com.ias.gsscore.viewmodelfactory.ActivityViewModelFactory

class EventsFragment() : Fragment() {
    lateinit var binding: FragmentEventsBinding
    lateinit var viewModel: EventsFragmentViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_events, container, false)
        viewModel = ViewModelProvider(this, ActivityViewModelFactory(binding,SingletonClass.instance))[EventsFragmentViewModel::class.java]
        binding.viewModel = viewModel
        initialData()
        return binding.root
    }

    private fun initialData() {
        viewModel.context = requireContext()
        viewModel.getChatData()
    }
}