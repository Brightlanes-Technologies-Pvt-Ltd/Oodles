package com.ias.gsscore.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.gson.Gson
import com.ias.gsscore.databinding.ActivityTestResultBinding
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.MaterialResponse
import com.ias.gsscore.network.response.testresult.Questions
import com.ias.gsscore.network.response.testresult.Report
import com.ias.gsscore.network.response.testresult.TestResultResponse
import com.ias.gsscore.ui.adapter.TestResultTopListAdapter
import com.ias.gsscore.ui.fragment.ResultDetailedAnalysisFragment
import com.ias.gsscore.ui.fragment.ResultRankFragment
import com.ias.gsscore.ui.fragment.ResultSectionalAnalysisFragment
import com.ias.gsscore.ui.fragment.ResultTestReportFragment
import com.ias.gsscore.ui.viewmodel.TestResultViewModel
import com.ias.gsscore.utils.Preferences
import com.ias.gsscore.viewmodelfactory.ActivityViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class TestResultActivity : AppCompatActivity(), TestResultTopListAdapter.ClickListener {
    lateinit var binding: ActivityTestResultBinding
    lateinit var viewModel: TestResultViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var adapterTop: TestResultTopListAdapter? = null
    private var topList = ArrayList<MaterialResponse>();
    private var clickListener: TestResultTopListAdapter.ClickListener? = null
    private var testId = ""
    private var programId = ""
    private var resultId = ""
    private val viewModelJob = Job()
    lateinit var loadingDialog: AlertDialog
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    private var reportData = Report()
    private var questionList = ArrayList<Questions>()
 
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityTestResultBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(
            this,
            ActivityViewModelFactory(binding, application)
        )[TestResultViewModel::class.java]

        setContentView(binding.root)
        binding.viewmodel = viewModel
        viewModel.context = this
        activity = this
        viewModel.supportFragmentManager = supportFragmentManager
        testId = intent.getStringExtra("testId").toString()
        programId = intent.getStringExtra("programId").toString()
        resultId = intent.getStringExtra("resultId").toString()
        viewModel.testId = testId
        viewModel.programId = programId
        binding.tvTitle.text = "Test Result"
        initView()
        setListeners()
    }

    private fun initView() {
        loadingDialog = RetrofitHelper.loadingDialog(this)
        clickListener = this
        setTopAdapter()
    }

    private fun setTopAdapter() {
        topList = getTopList();
        linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.rvMaterialTop.layoutManager = linearLayoutManager
        adapterTop = TestResultTopListAdapter(clickListener, topList)
        binding.rvMaterialTop.adapter = adapterTop
        prelimsTestReport()
    }

    @JvmName("getTopList1")
    private fun getTopList(): ArrayList<MaterialResponse> {
        var list = ArrayList<MaterialResponse>();
        list.add(MaterialResponse("Test Report", true))
        list.add(MaterialResponse("Sectional Analysis", false))
        list.add(MaterialResponse("Detailed Analysis", false))
        list.add(MaterialResponse("Rank", false))
        return list
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onClick(position: Int) {
        for ((index, value) in topList.withIndex()) {
            topList[index].bool = false;
        }
        topList[position].bool = true;
        adapterTop!!.notifyDataSetChanged()
        binding.layoutBottomMain.visibility = View.VISIBLE
        binding.layoutNextPreview.visibility = View.GONE
        when (topList[position].nameame) {
            "Test Report" -> viewModel.addFragment(ResultTestReportFragment(reportData), this,0,"")
            "Sectional Analysis" -> viewModel.addFragment(
                ResultSectionalAnalysisFragment(reportData),
                this,0,""
            )
            "Detailed Analysis" -> {
                if(questionList!=null){
                    viewModel.currentQuestionPos = 0
                    viewModel.addFragment(
                        ResultDetailedAnalysisFragment(
                            questionList[viewModel.currentQuestionPos],
                            reportData,
                            viewModel.currentQuestionPos,
                            questionList.size
                        ), this,0,""
                    )
                    binding.layoutBottomMain.visibility = View.GONE
                    binding.layoutNextPreview.visibility = View.VISIBLE

                }else{
                    Toast.makeText(this,"No question and section present!",Toast.LENGTH_SHORT).show()
                }

            }
            "Rank" -> viewModel.addFragment(ResultRankFragment(testId, programId, resultId), this,0,"")
        }
    }

    private fun setListeners() {
        binding.btNext.setOnClickListener {
            if (viewModel.currentQuestionPos < questionList.size-1) {
                viewModel.currentQuestionPos++
                viewModel.addFragment(
                    ResultDetailedAnalysisFragment(
                        questionList[viewModel.currentQuestionPos],
                        reportData,
                        viewModel.currentQuestionPos,
                        questionList.size
                    ), this,0,""
                )
            } else {
                Toast.makeText(this,"No more next questions",Toast.LENGTH_SHORT).show()
            }
        }
        binding.btPrevious.setOnClickListener {
            if (viewModel.currentQuestionPos>0) {
                viewModel.currentQuestionPos--
                viewModel.addFragment(
                    ResultDetailedAnalysisFragment(
                        questionList[viewModel.currentQuestionPos],
                        reportData,
                        viewModel.currentQuestionPos,
                        questionList.size
                    ), this,0,""
                )
            } else {
                Toast.makeText(this,"No more previous questions",Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun prelimsTestReport() {
        var request: HashMap<String, String> = HashMap()
        request["userId"] = Preferences.getInstance(this).userId
        request["testResultId"] = "1322157"
        request["testResultId"] = resultId
        request["programId"] = programId
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.prelimsTestReport(request)
            val response: TestResultResponse = result.body()!!
            Log.d("Test Report ====", Gson().toJson(response))
            if (response.status) {
                reportData = response.report!!
                questionList = reportData.questions
                viewModel.reportData = reportData
                viewModel.addFragment(ResultTestReportFragment(reportData), this@TestResultActivity,0,"")
            } else {
                Toast.makeText(
                    this@TestResultActivity,
                    response.error,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            loadingDialog.dismiss()
        }
    }

    companion object {
        var activity: TestResultActivity? = null
        fun getInstance(): TestResultActivity {
            return activity!!
        }
    }
}