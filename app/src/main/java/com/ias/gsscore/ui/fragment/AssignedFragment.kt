package com.ias.gsscore.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ias.gsscore.R
import com.ias.gsscore.databinding.FragmentAssignedBinding
import com.ias.gsscore.ui.viewmodel.AssignedFragmentViewModel
import com.ias.gsscore.utils.SingletonClass
import com.ias.gsscore.viewmodelfactory.ActivityViewModelFactory

class AssignedFragment() : Fragment() {
    lateinit var binding: FragmentAssignedBinding
    lateinit var viewModel: AssignedFragmentViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_assigned, container, false)
        viewModel = ViewModelProvider(this, ActivityViewModelFactory(binding,SingletonClass.instance))[AssignedFragmentViewModel::class.java]
        binding.viewModel = viewModel
        initialData()
        return binding.root
    }

    private fun initialData() {
        viewModel.context = requireContext()
        viewModel.binding = binding
        viewModel.getChatData()
    }
}