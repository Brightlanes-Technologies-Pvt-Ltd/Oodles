package com.ias.gsscore.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.ias.gsscore.R
import com.ias.gsscore.databinding.FragmentStudyNotesDetailsBinding
import com.ias.gsscore.ui.viewmodel.StudyNotesDetailsViewModel
import com.ias.gsscore.utils.SingletonClass
import com.ias.gsscore.utils.ZoomOutPageTransformer
import com.ias.gsscore.viewmodelfactory.ActivityViewModelFactory
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.ui.viewmodel.MainViewModel

class StudyNotesDetailsFragment : Fragment() {
    lateinit var binding: FragmentStudyNotesDetailsBinding
    lateinit var viewModel: StudyNotesDetailsViewModel

    var page = 0

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_study_notes_details, container, false)
        viewModel = ViewModelProvider(this, ActivityViewModelFactory(binding,
            SingletonClass.instance))[StudyNotesDetailsViewModel::class.java]
        binding.viewmodel = viewModel
        viewModel.productId = arguments?.getString("productId").toString()
        val title = arguments?.getString("title").toString()
        MainViewModel.setHeaderTitle(1,title)
        initialData()
        return binding.root
    }

    private fun initialData() {
        viewModel.context = requireContext()
        viewModel.loadingDialog = RetrofitHelper.loadingDialog(requireContext())
        viewModel.apiProductDetails()

        val zoomOutPageTransformer = ZoomOutPageTransformer()
        binding.viewPager2.setPageTransformer { page, position ->
            zoomOutPageTransformer.transformPage(page, position)
        }

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {

            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                page = position
            }

        })



    }





}