package com.ias.gsscore.ui.bottomsheet

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ias.gsscore.R
import com.ias.gsscore.databinding.BottomSheetModelAnswerBinding
import com.ias.gsscore.network.response.myaccount.QuestionData
import com.ias.gsscore.utils.Helpers

class ModelAnswerBottomSheet(private val data: QuestionData,private val bottomSheetInterface:BottomSheetInterface) : BottomSheetDialogFragment() {
    lateinit var binding: BottomSheetModelAnswerBinding
    companion object {
        const val TAG = "BottomSheet"
    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottom_sheet_model_answer, container, false)
        initView()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initView() {
        binding.apply {
            Helpers.setWebViewText(binding.tvQuestion,data.question!!)
            Helpers.setWebViewText(binding.tvSolution,data.solution!!)
            btBack.setOnClickListener {
                bottomSheetInterface.bottomSheetCLick()
            }
        }
    }

    interface  BottomSheetInterface{
        fun bottomSheetCLick()
    }
}