package com.ias.gsscore.ui.fragment

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.LinearLayout
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ias.gsscore.R
import com.ias.gsscore.databinding.FragmentResultTestReportBinding
import com.ias.gsscore.network.response.testresult.Report
import com.ias.gsscore.network.response.testresult.Sections
import com.ias.gsscore.network.response.testresult.Topics
import com.ias.gsscore.ui.adapter.AreaOfImprovementListAdapter
import kotlin.math.roundToInt


class ResultTestReportFragment(private val reportData: Report) : Fragment() {
    lateinit var binding: FragmentResultTestReportBinding
    private var sectionList = ArrayList<Sections>()
    private var allTopicSectionList = ArrayList<Topics>()
    private var areaOfImprovementList = ArrayList<Topics>()
    var spinnerSelectPos: Int = 0
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var adapterAreaOf: AreaOfImprovementListAdapter? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(
            inflater,
            R.layout.fragment_result_test_report,
            container,
            false
        )
        binding.testTitle.text = reportData.testTitle
        if (reportData.questions!=null && reportData.sections!=null){
            initialData()
        }else{
            Toast.makeText(context,"No question and section present!",Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun initialData() {
        setAreaOfImprovementAdapter()
        if (!reportData.percentile.equals("")){
            binding.tvPercentile.text = "Percentile : ${reportData.percentile}%"
            binding.progressPercentile.progress = (reportData.percentile!!.toFloat()).roundToInt()
        }else{
            binding.tvPercentile.text = "Percentile : 0%"
            binding.progressPercentile.progress = 0
        }
        //Performance
        val progressBlue = (reportData.myScore!!.toFloat()/100)
        if(progressBlue<=0){
            binding.progressGreen.background = ContextCompat.getDrawable(requireContext(),R.drawable.progress_green_left_round)
        }
        binding.tvCorrectBlue.text = "Your Score("+String.format("%.2f", reportData.myScore!!.toDouble())+")"
        val progressGreen = (reportData.avgScore!!.toFloat()/100)
        binding.tvAverageGreen.text = "Average Score("+ String.format("%.2f", reportData.avgScore!!.toDouble())+")"
        val progressYellow = (reportData.bestScore!!.toFloat()/100)
        binding.tvBestScoreYellow.text = "Best Score("+String.format("%.2f", reportData.bestScore!!.toDouble())+")"
        val mainProgress = (progressBlue+progressGreen+progressYellow)/100
        if(mainProgress<=0)
            binding.progressYellow.background = ContextCompat.getDrawable(requireContext(),R.drawable.progress_yellow_right_round)
        println("progressBlue: $progressBlue  progressGreen: $progressGreen  progressYellow: $progressYellow  mainProgress: $mainProgress")
        binding.progressBlue.layoutParams = funProgressSet(progressBlue)
        binding.progressGreen.layoutParams = funProgressSet(progressGreen)
        binding.progressYellow.layoutParams = funProgressSet(progressYellow)
        binding.progressMain.layoutParams = funProgressSet(mainProgress)

        binding.tvRank.text = "${reportData.rank}/${reportData.totalStudent}"
        binding.tvAttempt.text = "${reportData.attemptedQuestions}/${reportData.totalQuestions}"
        binding.tvAccuracy.text = "${reportData.accuracy}%"
        binding.tvProductiveTime.text = reportData.productiveTime
        binding.tvUnProductiveTime.text = reportData.unProductiveTime
        //  Accuracy Analysis First Time
        funAccuracyAnalysisSet(reportData.correctQuestions!!.toFloat(),reportData.incorrectQuestions!!.toFloat(),reportData.unattemptedQuestions!!.toFloat())

        sectionList = reportData.sections
        var spinnerList: Array<String?> = arrayOfNulls(sectionList.size + 1)
        spinnerList[0] = "All"
        for (i in sectionList.indices) {
            spinnerList[i + 1] = sectionList[i].title
            allTopicSectionList!!.addAll(sectionList[i].topics)
        }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item, spinnerList
        )
        binding.spinner.adapter = adapter

        binding.spinner.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                spinnerSelectPos = position
                if (position == 0) {
                    binding.tvTrueMarks.text = "" + reportData.trueScore
                    binding.tvActualMarks.text = ""
                    binding.tvNegativeMarks.text ="${reportData.negativeScore} Negative Marks"
                    setAreaOfImprovement(allTopicSectionList)
                    //  Accuracy Analysis First Time
                    funAccuracyAnalysisSet(reportData.correctQuestions!!.toFloat(),reportData.incorrectQuestions!!.toFloat(),reportData.unattemptedQuestions!!.toFloat())
                    //  Attempt Analysis First Time
                    funAttemptedAnalysisSet(reportData.attemptLog!!.fluke!!.toFloat(),reportData.attemptLog!!.waste!!.toFloat(),reportData.correctQuestions!!.toFloat())

                } else {
                    binding.tvTrueMarks.text = "" + sectionList[position - 1].score!!.trueScore
                    binding.tvActualMarks.text = sectionList[position - 1].score!!.myScore
                    binding.tvNegativeMarks.text = "${sectionList[position - 1].score!!.negativeScore} Negative Marks"
                    setAreaOfImprovement(sectionList[position - 1].topics)
                    //  Accuracy Analysis
                    funAccuracyAnalysisSet(sectionList[position - 1].attempt!!.correct!!.toFloat(),sectionList[position - 1].attempt!!.incorrect!!.toFloat(),sectionList[position - 1].attempt!!.unattempted!!.toFloat())
                    //  Attempt Analysis
                    funAttemptedAnalysisSet(sectionList[position - 1].attempt!!.fluke!!.toFloat(),sectionList[position - 1].attempt!!.waste!!.toFloat(),sectionList[position - 1].attempt!!.correct!!.toFloat())

                }

            }

            override fun onNothingSelected(parent: AdapterView<*>) {
                // write code to perform some action
            }
        }

        //  Attempt Analysis
        funAttemptedAnalysisSet(reportData.attemptLog!!.fluke!!.toFloat(),reportData.attemptLog!!.waste!!.toFloat(),reportData.correctQuestions!!.toFloat())


    }

    @SuppressLint("SetTextI18n")
    private fun funAttemptedAnalysisSet(
        progressFluke: Float,
        progressWaste: Float,
        progressCorrectAttempt: Float,
    ) {
        binding.tvAttemptBLue.text = "Fluke("+String.format("%.2f", progressFluke).toDouble()+")"
        binding.tvAttemptGreen.text = "Correct("+String.format("%.2f", progressCorrectAttempt).toDouble()+")"
        binding.tvAttemptRed.text = "Waste("+String.format("%.2f", progressWaste).toDouble()+")"
        val mainProgressAttempt = reportData.questions.size.toFloat()-(progressFluke+progressWaste+progressCorrectAttempt)
        binding.progressFluke.layoutParams = funProgressSet(progressFluke)
        if (progressFluke<=0f)
            binding.progressIncorrect.background = ContextCompat.getDrawable(requireContext(),R.drawable.progress_red_left_round)
        binding.progressWaste.layoutParams = funProgressSet(progressWaste)
        binding.progressCorrectAttempt.layoutParams = funProgressSet(progressCorrectAttempt)
        binding.progressMainAttempt.layoutParams = funProgressSet(mainProgressAttempt)
        if (mainProgressAttempt<=0f)
            binding.progressCorrectAttempt.background = ContextCompat.getDrawable(requireContext(),R.drawable.progress_green_right_round)

    }

    @SuppressLint("SetTextI18n")
    private fun funAccuracyAnalysisSet(
        progressCorrect: Float,
        progressIncorrect: Float,
        progressUnAttempt: Float,
    ) {
        binding.tvActualGreen.text = "Correct ("+String.format("%.2f", progressCorrect).toDouble()+")"
        binding.tvActualRed.text = "Incorrect ("+String.format("%.2f", progressIncorrect).toDouble()+")"
        binding.tvActualGrey.text = "Unattempted ("+String.format("%.2f", progressUnAttempt).toDouble()+")"
        if (reportData.questions!=null){
            val mainProgressAccuracy = reportData.questions.size.toFloat()-(progressCorrect+progressIncorrect+progressUnAttempt)
            binding.progressCorrect.layoutParams = funProgressSet(progressCorrect)
            if (progressCorrect<=0f)
                binding.progressIncorrect.background = ContextCompat.getDrawable(requireContext(),R.drawable.progress_red_left_round)
            binding.progressIncorrect.layoutParams = funProgressSet(progressIncorrect)
            binding.progressUnattempted.layoutParams = funProgressSet(progressUnAttempt)
            binding.progressMainAccuracy.layoutParams = funProgressSet(mainProgressAccuracy)
            if (mainProgressAccuracy<=0f)
                binding.progressUnattempted.background = ContextCompat.getDrawable(requireContext(),R.drawable.progress_dark_grey_right_round)
        }

    }

    private fun funProgressSet(progressBlue: Float): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.MATCH_PARENT,
            progressBlue
        )
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun setAreaOfImprovement(allTopicSectionList: ArrayList<Topics>) {
        areaOfImprovementList = ArrayList()
        for (item in allTopicSectionList) {
            val percentage = (item.correct * 100) / item.total
            if (percentage < 60) {
                item.progressPercentage = 60 - (percentage.toFloat().roundToInt())
                areaOfImprovementList.add(item)
            }
        }
        adapterAreaOf!!.updateList(areaOfImprovementList)
    }

    private fun setAreaOfImprovementAdapter() {
        linearLayoutManager = LinearLayoutManager(requireContext())
        binding.rvRecyclerView.layoutManager = linearLayoutManager
        adapterAreaOf = AreaOfImprovementListAdapter(areaOfImprovementList)
        binding.rvRecyclerView.adapter = adapterAreaOf
    }
}