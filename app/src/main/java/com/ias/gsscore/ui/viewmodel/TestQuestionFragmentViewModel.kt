package com.ias.gsscore.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.ias.gsscore.databinding.FragmentTestQuestionBinding
import com.ias.gsscore.network.request.QuestionRequest
import com.ias.gsscore.network.response.myaccount.OptionData
import com.ias.gsscore.network.response.myaccount.Questions
import com.ias.gsscore.ui.adapter.OptionListAdapter
import com.ias.gsscore.utils.Helpers

class TestQuestionFragmentViewModel(binding: ViewDataBinding) : ViewModel() {
    var binding = binding as FragmentTestQuestionBinding
    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context
    var optionListAdapter: OptionListAdapter = OptionListAdapter()
    @RequiresApi(Build.VERSION_CODES.N)
    fun getOptionData(
        questionData: Questions,
        answeredHashMap: HashMap<Int, QuestionRequest>,
        currentQuestionPos: Int
    ) {
        var optionList =
            listOf(
                OptionData(questionData.option1.toString(),false),
                OptionData(questionData.option2.toString(),false),
                OptionData(questionData.option3.toString(),false),
                OptionData(questionData.option4.toString(),false)
            )
        optionListAdapter.updateList(context,optionList,answeredHashMap,currentQuestionPos)
        Helpers.setWebViewText( binding.tvQuestion,questionData.question!!)
        Helpers.setWebViewText(binding.tvInstructions,questionData.instruction!!)

    }

    fun notifyAdapter(){
        optionListAdapter.clearAnswer()
    }
}