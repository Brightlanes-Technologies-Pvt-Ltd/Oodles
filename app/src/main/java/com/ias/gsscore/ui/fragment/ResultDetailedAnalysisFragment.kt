package com.ias.gsscore.ui.fragment

import android.annotation.SuppressLint
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ias.gsscore.R
import com.ias.gsscore.databinding.FragmentResultDetailsAnalysisBinding
import com.ias.gsscore.network.response.testresult.Questions
import com.ias.gsscore.network.response.testresult.Report
import com.ias.gsscore.ui.adapter.ResultOptionListAdapter
import com.ias.gsscore.utils.Helpers

class ResultDetailedAnalysisFragment(
    private val questionData: Questions,
    private val reportData: Report,
    private val currentQuestionPos: Int,
    private val questionSize: Int
) : Fragment() {
    lateinit var binding: FragmentResultDetailsAnalysisBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var adapter: ResultOptionListAdapter? = null
    private val optionPosition = arrayOf("A", "B", "C", "D", "E", "F")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_result_details_analysis, container, false)
        initialData()
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("SetTextI18n")
    private fun initialData() {
        binding.tvTitle.text = questionData.topic
        Helpers.setWebViewText(binding.tvQuestion,questionData.questionTitle!!)
        Helpers.setWebViewText(binding.tvInstructions,questionData.questionInstruction!!)
        binding.tvCorrectAns.text = optionPosition[questionData.correctAnswer!!.toInt()-1]
       // binding.tvQuestionProgress.text = "Question ${reportData.attemptedQuestions}/${reportData.totalQuestions}"
       // binding.progressBar.progress = (reportData.attemptedQuestions!!*100)/reportData.totalQuestions!!
        val questionsPos = currentQuestionPos+1
        binding.tvQuestionProgress.text = "Question ${questionsPos}/${questionSize}"
        binding.progressBar.progress = (questionsPos*100)/questionSize

        //Below Question UI
        binding.attemptType.text = questionData.attemptType
        binding.attemptMessage.text = questionData.message

        // You Vs Topper
        binding.tvYou.text ="${questionData.userSolvingTime} sec"
        binding.tvTopper.text = "${reportData.topperResponse?.get(questionData.questionId)?.solvingTime} sec"
        binding.tvIdealTime.text = "${questionData.solvingTime} sec"
        setAdapterOption()

        // Question Status
        binding.tvTopic.text = questionData.topic
        binding.tvSubTopic.text = questionData.subTopic
        binding.tvDifficultyLevel.text = questionData.level
        binding.tvCorrectPercentage.text = "%"

        // Solution
        Helpers.setWebViewText(binding.tvSolution,questionData.soultionTraditional!!)
    }

    private fun setAdapterOption() {
        var optionList =
            listOf(
                questionData.optionOne,
                questionData.optionTwo,
                questionData.optionThree,
                questionData.optionFour
            )

        linearLayoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.layoutManager = linearLayoutManager
        adapter = ResultOptionListAdapter(optionList,questionData,requireContext())
        binding.recyclerView.adapter = adapter
    }
}