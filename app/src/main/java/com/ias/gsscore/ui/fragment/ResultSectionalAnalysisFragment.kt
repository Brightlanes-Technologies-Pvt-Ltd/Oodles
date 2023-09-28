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
import com.ias.gsscore.databinding.FragmentResultSectionalAnalysisBinding
import com.ias.gsscore.network.response.testresult.Report
import com.ias.gsscore.network.response.testresult.Sections
import com.ias.gsscore.network.response.testresult.Topics
import com.ias.gsscore.ui.adapter.AreaOfImprovementListAdapter
import kotlin.math.roundToInt

class ResultSectionalAnalysisFragment(private val reportData: Report) : Fragment() {
    lateinit var binding: FragmentResultSectionalAnalysisBinding
    private var sectionList = ArrayList<Sections>()
    private var topicSectionList = ArrayList<Topics>()
    private var areaOfImprovementList = ArrayList<Topics>()
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
            R.layout.fragment_result_sectional_analysis,
            container,
            false
        )
        if (reportData.questions!=null && reportData.sections!=null){
            initialData()
        }else{
            Toast.makeText(context,"No question and section present!", Toast.LENGTH_SHORT).show()
        }

        return binding.root
    }

    private fun initialData() {
        setAreaOfImprovementAdapter()
        sectionList = reportData.sections
        var spinnerList: Array<String?> = arrayOfNulls(sectionList.size)
        for (i in sectionList.indices) {
            spinnerList[i] = sectionList[i].title
        }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item, spinnerList
        )
        binding.spinnerSection.adapter = adapter

        binding.spinnerSection.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            @SuppressLint("SetTextI18n")
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                binding.tvTrueMarks.text = "" + sectionList[position].score!!.trueScore
                binding.tvActualMarks.text = sectionList[position].score!!.myScore
                binding.tvNegativeMarks.text =
                    "${sectionList[position].score!!.negativeScore} Negative Marks"
                //  Accuracy Analysis
                funAccuracyAnalysisSet(
                    sectionList[position].attempt!!.correct!!.toFloat(),
                    sectionList[position].attempt!!.incorrect!!.toFloat(),
                    sectionList[position].attempt!!.unattempted!!.toFloat()
                )
                //  Attempt Analysis
                funAttemptedAnalysisSet(
                    sectionList[position].attempt!!.fluke!!.toFloat(),
                    sectionList[position].attempt!!.waste!!.toFloat(),
                    sectionList[position].attempt!!.correct!!.toFloat()
                )
                setTopicSpinnerData(position)
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
    }


    private fun funAttemptedAnalysisSet(
        progressFluke: Float,
        progressWaste: Float,
        progressCorrectAttempt: Float,
    ) {
        val mainProgressAttempt =
            reportData.questions.size.toFloat() - (progressFluke + progressWaste + progressCorrectAttempt)
        binding.progressFluke.layoutParams = funProgressSet(progressFluke)
        if (progressFluke <= 0f)
            binding.progressIncorrect.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.progress_red_left_round)
        binding.progressWaste.layoutParams = funProgressSet(progressWaste)
        binding.progressCorrectAttempt.layoutParams = funProgressSet(progressCorrectAttempt)
        binding.progressMainAttempt.layoutParams = funProgressSet(mainProgressAttempt)
        if (mainProgressAttempt <= 0f)
            binding.progressCorrectAttempt.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.progress_green_right_round)

    }

    private fun funAccuracyAnalysisSet(
        progressCorrect: Float,
        progressIncorrect: Float,
        progressUnAttempt: Float,
    ) {
        val mainProgressAccuracy =
            reportData.questions.size.toFloat() - (progressCorrect + progressIncorrect + progressUnAttempt)
        binding.progressCorrect.layoutParams = funProgressSet(progressCorrect)
        if (progressCorrect <= 0f)
            binding.progressIncorrect.background =
                ContextCompat.getDrawable(requireContext(), R.drawable.progress_red_left_round)
        binding.progressIncorrect.layoutParams = funProgressSet(progressIncorrect)
        binding.progressUnattempted.layoutParams = funProgressSet(progressUnAttempt)
        binding.progressMainAccuracy.layoutParams = funProgressSet(mainProgressAccuracy)
        if (mainProgressAccuracy <= 0f)
            binding.progressUnattempted.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.progress_dark_grey_right_round
            )
    }

    private fun funTopicWiseAnalysisSet(
        progressCorrect: Float,
        progressIncorrect: Float,
        progressUnAttempt: Float,
    ) {
        val mainProgressAccuracy =
            reportData.questions.size.toFloat() - (progressCorrect + progressIncorrect + progressUnAttempt)
        binding.progressCorrectTopic.layoutParams = funProgressSet(progressCorrect)
        binding.progressIncorrectTopic.layoutParams = funProgressSet(progressIncorrect)
        binding.progressUnattemptedTopic.layoutParams = funProgressSet(progressUnAttempt)
        binding.progressMainTopic.layoutParams = funProgressSet(mainProgressAccuracy)
        if (progressCorrect <= 0)
            binding.progressIncorrectTopic.background = ContextCompat.getDrawable(requireContext(), R.drawable.progress_red_left_round)
        else
            binding.progressIncorrectTopic.background = ContextCompat.getDrawable(requireContext(), R.drawable.progress_red)
        if (mainProgressAccuracy <= 0)
            binding.progressUnattemptedTopic.background = ContextCompat.getDrawable(
                requireContext(),
                R.drawable.progress_dark_grey_right_round
            )
    }


    private fun funProgressSet(progressBlue: Float): LinearLayout.LayoutParams {
        return LinearLayout.LayoutParams(
            0,
            LinearLayout.LayoutParams.MATCH_PARENT,
            progressBlue
        )
    }


    private fun setTopicSpinnerData(position: Int) {
        topicSectionList = sectionList[position].topics
        setAreaOfImprovement(topicSectionList)
        var spinnerTopics: Array<String?> = arrayOfNulls(topicSectionList.size)
        for (i in topicSectionList.indices) {
            spinnerTopics[i] = topicSectionList[i].title
        }
        val adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item, spinnerTopics
        )
        binding.spinnerTopic.adapter = adapter

        binding.spinnerTopic.onItemSelectedListener = object :
            AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View, position: Int, id: Long
            ) {
                println()
                // val title = topicSectionList[position].title
                funTopicWiseAnalysisSet(
                    topicSectionList[position].correct.toFloat(),
                    topicSectionList[position].incorrect.toFloat(),
                    topicSectionList[position].unattempted.toFloat()
                )

            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }
        }
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