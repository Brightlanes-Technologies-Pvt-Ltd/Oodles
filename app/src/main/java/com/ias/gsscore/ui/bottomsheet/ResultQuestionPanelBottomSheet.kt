package com.ias.gsscore.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ias.gsscore.R
import com.ias.gsscore.databinding.BottomsheetQuestionPanelBinding
import com.ias.gsscore.network.response.testresult.Questions
import com.ias.gsscore.network.response.testresult.Report
import com.ias.gsscore.ui.adapter.ResultQuestionPanelListAdapter

class ResultQuestionPanelBottomSheet(
    private val contextActivity: FragmentActivity,
    private val reportData: Report
) : BottomSheetDialogFragment() {
    lateinit var binding: BottomsheetQuestionPanelBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var adapter: ResultQuestionPanelListAdapter? = null
    private var questionList = ArrayList<Questions>()
    companion object {
        const val TAG = "QuestionPanelBottomSheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_question_panel, container, false)
        initView()
        return binding.root
    }

    private fun initView() {
        questionList = reportData.questions
        linearLayoutManager = GridLayoutManager(context,7)
        binding.rvQuestionPanel.layoutManager = linearLayoutManager
        adapter = context?.let { ResultQuestionPanelListAdapter(it,contextActivity,questionList,reportData) }
        binding.rvQuestionPanel.adapter = adapter
    }
}