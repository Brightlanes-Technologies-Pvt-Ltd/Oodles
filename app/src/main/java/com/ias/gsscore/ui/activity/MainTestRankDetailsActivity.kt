package com.ias.gsscore.ui.activity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.R
import com.ias.gsscore.databinding.ActivityMainTestRankDetailsBinding
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.myaccount.QuestionData
import com.ias.gsscore.network.response.myaccount.TopperData
import com.ias.gsscore.network.response.testresult.TestResultResponse
import com.ias.gsscore.ui.adapter.MainTestRankListAdapter
import com.ias.gsscore.ui.adapter.MainsTestQuestionListListAdapter
import com.ias.gsscore.ui.bottomsheet.BatchListBottomSheet
import com.ias.gsscore.ui.bottomsheet.ModelAnswerBottomSheet
import com.ias.gsscore.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainTestRankDetailsActivity : AppCompatActivity(),
    MainsTestQuestionListListAdapter.ModelAnswerInterface,
    ModelAnswerBottomSheet.BottomSheetInterface {
    lateinit var binding : ActivityMainTestRankDetailsBinding
    private val viewModelJob = Job()
    lateinit var loadingDialog: AlertDialog
    private val coroutineScope = CoroutineScope(Dispatchers.Main+viewModelJob)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    private var testId = ""
    private var adapter: MainTestRankListAdapter? = null
    private var adapterQuestionList: MainsTestQuestionListListAdapter? = null
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var toppersList: ArrayList<TopperData> = arrayListOf()
    private var questionList: ArrayList<QuestionData> = arrayListOf()
    private lateinit var whereFrom:String
    private var hasDiscussionVideo:String = ""
    private var breakup:String = ""
    private lateinit var modelAnswerInterface: MainsTestQuestionListListAdapter.ModelAnswerInterface
    private lateinit var bottomSheetInterface: ModelAnswerBottomSheet.BottomSheetInterface
    private var bottomSheetModelAnswer: ModelAnswerBottomSheet? = null

    @SuppressLint("SetJavaScriptEnabled")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main_test_rank_details)
        setContentView(binding.root)
        modelAnswerInterface = this
        bottomSheetInterface = this
        loadingDialog = RetrofitHelper.loadingDialog(this)
        val title = intent.getStringExtra("title")
        hasDiscussionVideo = intent.getStringExtra("hasDiscussionVideo").toString()
        breakup = intent.getStringExtra("breakup").toString()


        binding.tvTitle.text = title;
        testId = intent.getStringExtra("testId").toString()
        whereFrom = intent.getStringExtra("whereFrom").toString()
        binding.ivBack.setOnClickListener { finish() }
        initialData()
    }

    private fun initialData() {
        loadingDialog = RetrofitHelper.loadingDialog(this)
        if (whereFrom == "TopperRank") {
            topperListApiCall()
            binding.layoutRank.visibility = View.VISIBLE
        }
        else {
            questionListApiCall()
            binding.layoutRank.visibility = View.GONE
        }
    }

    private fun topperListApiCall() {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(this).userId
        request["test_id"] = testId
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.mainsTestTopperList(request)
            val response: TestResultResponse = result.body()!!
            if (response.status) {
                if (response.toppersList.size>0){
                    toppersList = response.toppersList
                    setAdapter(toppersList,questionList)
                }
                else
                    Toast.makeText(
                        this@MainTestRankDetailsActivity,
                        "Data not found",
                        Toast.LENGTH_SHORT
                    )
                        .show()
            } else {
                Toast.makeText(
                    this@MainTestRankDetailsActivity,
                    response.error,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            loadingDialog.dismiss()
        }
    }

    private fun questionListApiCall() {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(this).userId
        request["test_id"] = testId
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.mainsTestQuestionList(request)
            val response: TestResultResponse = result.body()!!
            if (response.status) {
                if (response.questionList.size>0){
                    questionList = response.questionList
                    setAdapter(toppersList,questionList)
                }
                else
                    Toast.makeText(
                        this@MainTestRankDetailsActivity,
                        "Data not found",
                        Toast.LENGTH_SHORT
                    )
                        .show()
            } else {
                Toast.makeText(
                    this@MainTestRankDetailsActivity,
                    response.error,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            loadingDialog.dismiss()
        }
    }

    private fun setAdapter(toppersList: ArrayList<TopperData>, questionList: ArrayList<QuestionData>) {
        linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = linearLayoutManager
        if (whereFrom == "TopperRank"){
            adapter = MainTestRankListAdapter(toppersList)
            binding.recyclerView.adapter = adapter
        }else{
            adapterQuestionList = MainsTestQuestionListListAdapter(this,questionList,hasDiscussionVideo,breakup,modelAnswerInterface)
            binding.recyclerView.adapter = adapterQuestionList
        }


        binding.recyclerView.addOnScrollListener(object :
            RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    /*  visibleItemCount = linearLayoutManager.childCount
                      totalItemCount = linearLayoutManager.itemCount
                      pastVisiblesItems = linearLayoutManager.findFirstCompletelyVisibleItemPosition()
                      if (loading) {
                          if (visibleItemCount + pastVisiblesItems >= totalItemCount) {
                              val jsonObject = JSONObject()
                              try {
                                  loading = false
                                  pageNo += 1
                                  prelimsLeaderboardApiCall()
                              } catch (e: JSONException) {
                                  e.printStackTrace()
                              }
                          }
                      }*/
                }
            }
        })


    }

    override fun modelAnswerCLick(data: QuestionData) {
        bottomSheetModelAnswer =
            ModelAnswerBottomSheet(data,bottomSheetInterface).apply {
                show(supportFragmentManager!!, BatchListBottomSheet.TAG)
            }

    }

    override fun bottomSheetCLick() {
        bottomSheetModelAnswer!!.dismiss()
    }
}