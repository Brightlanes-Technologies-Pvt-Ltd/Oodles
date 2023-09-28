package com.ias.gsscore.ui.bottomsheet

import android.annotation.SuppressLint
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
import com.ias.gsscore.network.request.QuestionRequest
import com.ias.gsscore.network.response.myaccount.StartTestResponse
import com.ias.gsscore.ui.adapter.QuestionPanelListAdapter

class QuestionPanelBottomSheet(
    private val startTestResponse: StartTestResponse,
    private val answeredHashMap: HashMap<Int, QuestionRequest>,
    private val contextActivity: FragmentActivity
) : BottomSheetDialogFragment() {
    lateinit var binding: BottomsheetQuestionPanelBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var adapter: QuestionPanelListAdapter? = null

    companion object {
        const val TAG = "QuestionPanelBottomSheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.bottomsheet_question_panel, container, false)
        initView()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        linearLayoutManager = GridLayoutManager(context, 7)
        binding.rvQuestionPanel.layoutManager = linearLayoutManager
        adapter = context?.let {
            QuestionPanelListAdapter(
                it,
                answeredHashMap,
                startTestResponse,
                contextActivity
            )
        }
        binding.rvQuestionPanel.adapter = adapter
        var answered = 0
        var markForReview = 0
        var notAnswered = 0
        var notVisited = 0
        for ((key, value) in answeredHashMap) {
            when (value!!.attemptType) {
                "answered" -> {
                    answered++
                }
                "markForReview" -> {
                    markForReview++
                }
                "notAnswered" -> {
                    notAnswered++
                }
                else -> {
                    notVisited++
                }
            }
        }
        binding.tvMarkView.text = "Marked for Review ($markForReview)"
        binding.tvAnswered.text = "Answered ($answered)"
        binding.tvNotAnswered.text = "Not Answered ($notAnswered)"
        binding.tvNotVisited.text = "Not Visited ($notVisited)"
    }
}