package com.ias.gsscore.ui.fragment

import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.ias.gsscore.R
import com.ias.gsscore.databinding.FragmentTestQuestionBinding
import com.ias.gsscore.network.request.QuestionRequest
import com.ias.gsscore.network.response.myaccount.Questions
import com.ias.gsscore.ui.viewmodel.TestQuestionFragmentViewModel
import com.ias.gsscore.utils.SingletonClass
import com.ias.gsscore.viewmodelfactory.ActivityViewModelFactory

class TestQuestionFragment(
    private val questionData: Questions,
    private val answeredHashMap: HashMap<Int, QuestionRequest>,
    private val currentQuestionPos: Int
) : Fragment() {
    lateinit var binding: FragmentTestQuestionBinding
    lateinit var viewModel: TestQuestionFragmentViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_test_question, container, false)
        viewModel = ViewModelProvider(this, ActivityViewModelFactory(binding,SingletonClass.instance))[TestQuestionFragmentViewModel::class.java]
        binding.viewModel = viewModel
        initialData()
        return binding.root
    }

    private fun initialData() {
        viewModel.context = requireContext()
        viewModel.binding = binding
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            viewModel.getOptionData(questionData,answeredHashMap,currentQuestionPos)
        }
        val data:QuestionRequest = answeredHashMap[currentQuestionPos]!!
        if (data.attemptType=="notVisited") {
            data.attemptType = "notAnswered"
            answeredHashMap[currentQuestionPos] = data
        }
        binding.tvClear.setOnClickListener {
            val data:QuestionRequest = answeredHashMap[currentQuestionPos]!!
            if (data.attemptType=="answered") {
                data.attemptType = "notAnswered"
                data.selectedOption = ""
                answeredHashMap[currentQuestionPos] = data
                viewModel.notifyAdapter()
            }
        }
    }
}