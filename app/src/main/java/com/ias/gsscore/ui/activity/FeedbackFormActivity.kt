package com.ias.gsscore.ui.activity

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.ias.gsscore.databinding.ActivityFeedbackFormBinding
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.OodlesApiResponse
import com.ias.gsscore.network.response.myaccount.FeedbackData
import com.ias.gsscore.ui.adapter.FeedbackListAdapter
import com.ias.gsscore.ui.viewmodel.FeedbackFormViewModel
import com.ias.gsscore.utils.Preferences
import com.ias.gsscore.viewmodelfactory.ActivityViewModelFactory
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class FeedbackFormActivity : AppCompatActivity() {
    lateinit var binding: ActivityFeedbackFormBinding
    private val viewModelJob = Job()
    lateinit var loadingDialog: AlertDialog
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    private lateinit var linearLayoutManager: LinearLayoutManager
    lateinit var viewModel: FeedbackFormViewModel
    private var adapter: FeedbackListAdapter? = null
    private lateinit var feedbackList: List<FeedbackData>
    private var testId = ""
    private var programId = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFeedbackFormBinding.inflate(layoutInflater)
        viewModel = ViewModelProvider(
            this,
            ActivityViewModelFactory(binding, application)
        )[FeedbackFormViewModel::class.java]

        setContentView(binding.root)
        binding.viewmodel = viewModel
        viewModel.context = this
        binding.tvTitle.text = "Feedback Form"
        testId = intent.getStringExtra("testId").toString()
        programId = intent.getStringExtra("programId").toString()
        initView()
        setListeners()
    }

    private fun initView() {
        feedbackList =
            listOf(
                FeedbackData("Comprehensive Coverage of Topics", -1),
                FeedbackData("UPSC Oriented Question Framing &amp; Quality of Options", -1),
                FeedbackData("Improvement in conceptual clarity", -1),
                FeedbackData("Fluency in Reading", -1),
                FeedbackData("Quality of Supplementary notes", -1),
                FeedbackData("Opportunity for practicing Elimination Skills", -1),
            )

        linearLayoutManager = LinearLayoutManager(this)
        binding.recyclerView.layoutManager = linearLayoutManager
        adapter = FeedbackListAdapter()
        binding.recyclerView.adapter = adapter
        adapter!!.updateList(feedbackList)
    }

    private fun setListeners() {
        loadingDialog = RetrofitHelper.loadingDialog(this)
        binding.btSubmit.setOnClickListener {
            for (item in feedbackList) {
                if (item.id == -1) {
                    Toast.makeText(this, "Please select ${item.name}", Toast.LENGTH_LONG).show()
                    return@setOnClickListener
                }
            }
            if (binding.tvMessage.text.toString().isEmpty()) {
                Toast.makeText(this, "Please enter message", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            var request: HashMap<String, String> = HashMap()
            request["userId"] = Preferences.getInstance(this).userId
            request["testId"] = testId
            request["programId"] = programId
            request["quesId1"] = feedbackList[0].id.toString()
            request["quesId2"] = feedbackList[1].id.toString()
            request["quesId3"] = feedbackList[2].id.toString()
            request["quesId4"] = feedbackList[3].id.toString()
            request["quesId5"] = feedbackList[4].id.toString()
            request["quesId6"] = feedbackList[5].id.toString()
            request["message"] = binding.tvMessage.text.toString()
            submitPrelimsTestUserFeedback(request)
        }
    }

    private fun submitPrelimsTestUserFeedback(request: HashMap<String, String>) {
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.prelimsUserFeedback(request)
            val response: OodlesApiResponse = result.body()!!
            if (response.status) {
                Toast.makeText(
                    this@FeedbackFormActivity,
                    response.message,
                    Toast.LENGTH_SHORT
                )
                    .show()
                finish()
            } else {
                Toast.makeText(
                    this@FeedbackFormActivity,
                    response.error,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            loadingDialog.dismiss()
        }
    }

}