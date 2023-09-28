package com.ias.gsscore.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.ias.gsscore.R
import com.ias.gsscore.databinding.FragmentStudyNotesBinding
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.ui.adapter.StudyNotesCategoryAdapter
import com.ias.gsscore.ui.viewmodel.MainViewModel
import com.ias.gsscore.ui.viewmodel.StudyNotesViewModel
import com.ias.gsscore.utils.SingletonClass
import com.ias.gsscore.viewmodelfactory.ActivityViewModelFactory

class StudyNotesFragment : Fragment(), StudyNotesCategoryAdapter.ClickListener {
    lateinit var binding: FragmentStudyNotesBinding
    lateinit var viewModel: StudyNotesViewModel
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_study_notes, container, false)
        viewModel = ViewModelProvider(this,ActivityViewModelFactory(binding, SingletonClass.instance))[StudyNotesViewModel::class.java]
        initView()
        return binding.root
    }

    private fun initView() {
        viewModel.clickListener = this
        binding.viewmodel = viewModel
        viewModel.context = requireActivity()
        viewModel.loadingDialog = RetrofitHelper.loadingDialog(requireActivity())

        viewModel.apiProductList("FirstTime", "",viewModel.offSet)
        viewModel.init()
        viewModel.deliveryItemListApiCall()
        /* var gridLayoutManager = GridLayoutManager(context, 2, GridLayoutManager.VERTICAL, false)
         binding.rvStudyList.layoutManager = gridLayoutManager
         studyNotesListAdapter = context?.let { StudyNotesProductAdapter(it) }
         binding.rvStudyList.adapter = studyNotesListAdapter*/

    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(position: Int) {

        for ((index, value) in viewModel.categoryList.withIndex()) {
            viewModel.categoryList[index].bool = false;
        }
        viewModel.categoryList[position].bool = true;
        viewModel.adapterCategory.notifyDataSetChanged()
        if(position==0){
            viewModel.pageNo=0
            viewModel.m_id=""
            viewModel.where_from="ClickCategory"
            viewModel.apiProductList(
                "ClickCategory",
                "",
                viewModel.offSet
            )
        }else{
            viewModel.pageNo=0
            viewModel.m_id=viewModel.categoryList[position].id!!
            viewModel.where_from="ClickCategory"
            viewModel.apiProductList(
                "ClickCategory",
                viewModel.categoryList[position].id!!,
                viewModel.offSet
            )
        }

    }


    override fun onDestroy() {
        super.onDestroy()
        MainViewModel.setHeaderTitle(0,"")
    }

}