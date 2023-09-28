package com.ias.gsscore.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Build
import android.os.CountDownTimer
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ias.gsscore.R
import com.ias.gsscore.databinding.ActivityTakeTestBinding
import com.ias.gsscore.db.OodlesDB
import com.ias.gsscore.db.model.TestQuestionModel
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.request.QuestionRequest
import com.ias.gsscore.network.request.TestSubmitRequest
import com.ias.gsscore.network.response.OodlesApiResponse
import com.ias.gsscore.network.response.myaccount.StartTestResponse
import com.ias.gsscore.ui.activity.TakeTestActivity
import com.ias.gsscore.ui.activity.TestResultActivity
import com.ias.gsscore.ui.bottomsheet.QuestionPanelBottomSheet
import com.ias.gsscore.ui.fragment.TestQuestionFragment
import com.ias.gsscore.utils.Preferences
import kotlinx.coroutines.*
import java.time.LocalTime

class TakeTestViewModel(binding: ViewDataBinding) : ViewModel() {
    var binding = binding as ActivityTakeTestBinding

    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context
    lateinit var contextActivity: FragmentActivity
    lateinit var supportFragmentManager: FragmentManager
    lateinit var loadingDialog: AlertDialog
    lateinit var db: OodlesDB
    private var questionPanelBottomSheet: QuestionPanelBottomSheet? = null
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    var answeredHashMap: HashMap<Int, QuestionRequest> = HashMap()
    private var questionsOfflineList: MutableList<TestQuestionModel> = arrayListOf()
    var testIdList: List<String> = arrayListOf()
    private var currentQuestionPos: Int = 0
    lateinit var startTestResponse: StartTestResponse
    var spendTime = 0
    var totalSpendTime = 0
    lateinit var testId: String
    lateinit var programId: String
    fun clickExitTest() {
        dialogExitSubmit("Are you Sure you want to Exit your Test", "exit")
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clickMarkForReview() {
        val data: QuestionRequest = answeredHashMap[currentQuestionPos]!!
        /* if (data.attemptType=="answered"){
             Toast.makeText(
                 context,
                 "First clear answer",
                 Toast.LENGTH_SHORT
             )
                 .show()
         }*/
        data.attemptType = "markForReview"
        answeredHashMap[currentQuestionPos] = data
        Toast.makeText(
            context,
            "Question mark for review",
            Toast.LENGTH_SHORT
        )
            .show()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    fun clickNext() {
        if (startTestResponse.questionsList.size > 0 && (currentQuestionPos < (startTestResponse.questionsList.size - 1))) {
            setSpendTime()
            offLineDataBase()
            currentQuestionPos++
            // binding.tvTotalQuestion.text = "Question " + (currentQuestionPos) + "/" + startTestResponse.testDetails[0].totalQuestions
            setProgress(currentQuestionPos + 1)
            addFragment(
                TestQuestionFragment(
                    startTestResponse.questionsList[currentQuestionPos],
                    answeredHashMap,
                    currentQuestionPos
                ), contextActivity, currentQuestionPos, "next"
            )
        } else
            Toast.makeText(context, "No more questions", Toast.LENGTH_LONG).show()

    }

    @RequiresApi(Build.VERSION_CODES.O)
    @OptIn(DelicateCoroutinesApi::class)
    private fun offLineDataBase() {
        val dataQues: QuestionRequest = answeredHashMap[currentQuestionPos]!!
        coroutineScope.launch {
            loadingDialog.show()
            var isExit: TestQuestionModel? = null
            GlobalScope.launch(Dispatchers.IO) {
                isExit = db.oodlesDao().isQuestionExist(dataQues.questionId)
                if (isExit != null && isExit!!.questionId == dataQues.questionId) {
                    val data = TestQuestionModel(
                        isExit!!.id,
                        testId,
                        dataQues.questionId,
                        dataQues.attemptOrder,
                        dataQues.attemptType,
                        dataQues.selectedOption,
                        dataQues.solvingTime
                    )
                    GlobalScope.launch(Dispatchers.IO) {
                        db.oodlesDao().update(data)
                    }
                } else {
                    val data = TestQuestionModel(
                        0,
                        testId,
                        dataQues.questionId,
                        dataQues.attemptOrder,
                        dataQues.attemptType,
                        dataQues.selectedOption,
                        dataQues.solvingTime
                    )
                    GlobalScope.launch(Dispatchers.IO) {
                        db.oodlesDao().insert(data)
                    }
                }
                loadingDialog.dismiss()
            }
        }
    }

    private fun setSpendTime() {
        val data: QuestionRequest = answeredHashMap[currentQuestionPos]!!
        var previousSpendTime = data.solvingTime
        previousSpendTime += spendTime
        /* val str = previousSpendTime.split(":")
         var sec = str[1].toInt()+spendTime
         var min = str[0].toInt()
         if (sec>=60){
              min = sec/60
             sec %= 10
         }*/
        data.solvingTime = previousSpendTime
        answeredHashMap[currentQuestionPos] = data
        spendTime = 0
    }

    fun clickSubmitTest() {
        println("ppppppppp $answeredHashMap")
        dialogExitSubmit("Are you Sure you want to Submit your Test", "submit")
    }

    fun swipeToOpenQuestion() {
        questionPanelBottomSheet =
            QuestionPanelBottomSheet(startTestResponse, answeredHashMap, contextActivity).apply {
                show(supportFragmentManager, QuestionPanelBottomSheet.TAG)
            }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun startTest(testId: String, programId: String) {
        this.testId = testId
        this.programId = programId
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["test_id"] = testId
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.startTest(request)
            val response: StartTestResponse = result.body()!!
            if (response.status) {
                setAnsweredHasMap(response)

            } else {
                Toast.makeText(
                    context,
                    response.error,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            loadingDialog.dismiss()
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n")
    private fun setAnsweredHasMap(response: StartTestResponse) {
        startTestResponse = response;
        val testDetails = response.testDetails

        viewModelScope.launch(Dispatchers.IO) {
            questionsOfflineList = db.oodlesDao().getAll(testId)
            withContext(Dispatchers.Main) {
                var timeTaken: Int = 0
                for ((index, element) in response.questionsList.withIndex()) {
                    val request = QuestionRequest()
                    request.questionId = element.id!!
                    for (item in questionsOfflineList) {
                        if (element.id.equals(item.questionId)) {
                            request.attemptOrder = item.attemptOrder
                            request.attemptType = item.attemptType
                            request.selectedOption = item.selectedOption
                            request.solvingTime = item.solvingTime
                            timeTaken += item.solvingTime
                        }
                    }
                    answeredHashMap[index] = request
                }
                totalSpendTime = timeTaken
                if (testDetails.size > 0) {
                    binding.tvQuestionTitle.text = testDetails[0].title
                    val totalTimeSecond =
                        (startTestResponse.testDetails[0].duration!!.toInt() * 60 - timeTaken)
                    startTimeCounter(totalTimeSecond.toLong())
                }
                println(answeredHashMap)
                if (startTestResponse.questionsList.size > 0)
                    addFragment(
                        TestQuestionFragment(
                            response.questionsList[currentQuestionPos],
                            answeredHashMap,
                            currentQuestionPos
                        ), contextActivity, currentQuestionPos, "firstTime"
                    )
            }
        }

    }

    @SuppressLint("SetTextI18n")
    fun setProgress(currentQuestionPos: Int) {
        val totalQues = startTestResponse.questionsList.size
        val progress = 100 * (currentQuestionPos - 1) / totalQues
        binding.tvTotalQuestion.text = "Question $currentQuestionPos/$totalQues"
        if (binding.progressBar.progress < progress) {
            binding.tvProgress.text = "Progress : $progress%"
            binding.progressBar.progress = progress
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun addFragment(
        fragment: Fragment?,
        requireActivity: FragmentActivity,
        currentQuestionPos: Int,
        whereFrom: String
    ) {
        if (whereFrom == "questionPanel") {
            setSpendTime()
        }
        this.currentQuestionPos = currentQuestionPos
        if (startTestResponse.questionsList.size > 0) {
            setProgress(this.currentQuestionPos + 1)
        }
        if (fragment == null) return
        requireActivity.supportFragmentManager.beginTransaction()
            .replace(R.id.rootContainer, fragment)
            .commitAllowingStateLoss()
        try {
            questionPanelBottomSheet!!.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    private fun dialogExitSubmit(msg: String?, type: String) {
        try {
            val dialog = Dialog(context)
            dialog.window!!.requestFeature(1)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            dialog.setContentView(R.layout.dialog_exit_submit_test)
            dialog.setCancelable(false)
            val yes = dialog.findViewById<LinearLayout>(R.id.yes)
            val no = dialog.findViewById<LinearLayout>(R.id.no)
            val message = dialog.findViewById<TextView>(R.id.message)
            message.text = msg
            no.setOnClickListener { dialog.dismiss() }
            yes.setOnClickListener {
                dialog.dismiss()
                if (type == "exit")
                    (context as Activity).finish()
                else {
                    submitTestApi(type)
                }
            }
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun submitTestApi(type: String) {
        /* var ansFinalHashMap: HashMap<Int, String> = HashMap()
         for ((key,value) in answeredHashMap){
             val outputJson: String = Gson().toJson(value)
             ansFinalHashMap[key] = outputJson
         }*/
        val request = TestSubmitRequest()
        request.userId = Preferences.getInstance(context).userId
        request.testId = startTestResponse.testDetails[0].id!!
        request.testType = startTestResponse.testDetails[0].testType!!
        request.testResultId = startTestResponse.resultId!!
        val minute = totalSpendTime / 60
        val second = totalSpendTime % 60
        request.timeTaken = "$minute:$second"
        request.selectedQuestions = answeredHashMap
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.submitTest(request)
            val response: OodlesApiResponse = result.body()!!
            if (response.status) {
                GlobalScope.launch(Dispatchers.IO) {
                    db.oodlesDao().deleteTestId(testId)
                }
                dialogTestFinalSubmit(
                    "You have attempted ${answeredQuestionCount()} questions out of ${startTestResponse.questionsList.size}",
                    type
                )
            } else {
                Toast.makeText(
                    context,
                    response.error,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            loadingDialog.dismiss()
        }
    }

    private fun dialogTestFinalSubmit(msg: String, type: String) {
        try {
            val dialog = Dialog(context)
            dialog.window!!.requestFeature(1)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            dialog.setContentView(R.layout.dialog_test_final_submit)
            dialog.setCancelable(false)
            val ivExit = dialog.findViewById<ImageView>(R.id.ivExit)
            val viewResult = dialog.findViewById<LinearLayout>(R.id.viewResult)
            val takeMoreTest = dialog.findViewById<LinearLayout>(R.id.takeMoreTest)
            val message = dialog.findViewById<TextView>(R.id.message)
            message.text = msg
            if (type == "submit")
                ivExit.visibility = View.VISIBLE
            else
                ivExit.visibility = View.GONE
            ivExit.setOnClickListener {
                dialog.dismiss()
                val intent = Intent(context, TestResultActivity::class.java)
                intent.putExtra("testId", testId)
                intent.putExtra("programId", programId)
                intent.putExtra("resultId", startTestResponse.resultId)
                context.startActivity(intent)
                (context as Activity).finish()
            }
            viewResult.setOnClickListener {
                dialog.dismiss()
                val intent = Intent(context, TestResultActivity::class.java)
                intent.putExtra("testId", testId)
                intent.putExtra("programId", programId)
                intent.putExtra("resultId", startTestResponse.resultId)
                context.startActivity(intent)
                (context as Activity).finish()
            }
            takeMoreTest.setOnClickListener {
                dialog.dismiss()
                val intent = Intent(context, TakeTestActivity::class.java)
                intent.putExtra("title", startTestResponse.testDetails[0].title)
                intent.putExtra("testId", startTestResponse.testDetails[0].id)
                context.startActivity(intent)
                (context as Activity).finish()
            }
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    private fun startTimeCounter(timer: Long) {
        var counter: Long = timer
        object : CountDownTimer((counter * 1000), 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //binding.tvTimeLeft.text = counter.toString()
                binding.tvTimeLeft.text = timeConvert(counter)
                counter--
                spendTime++
                totalSpendTime++
            }

            @SuppressLint("SetTextI18n")
            override fun onFinish() {
                binding.tvTimeLeft.text = "Finished"
                submitTestApi("Finished")
            }
        }.start()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun timeConvert(nSecondTime: Long): String? {
        return LocalTime.MIN.plusSeconds(nSecondTime).toString()
    }

    fun answeredQuestionCount(): Int {
        var count = 0
        for ((key, value) in answeredHashMap) {
            if (value.attemptType == "answered")
                count++
        }
        return count
    }
    // Backup code for timer
    /* private  fun getDurationString(seconds: Int): String? {
         var seconds = seconds
         val hours = seconds / 3600
         val minutes = seconds % 3600 / 60
         seconds %= 60
         return twoDigitString(hours) + " : " + twoDigitString(minutes) + " : " + twoDigitString(
             seconds
         )
     }

     private fun twoDigitString(number: Int): String {
         if (number == 0) {
             return "00"
         }
         return if (number / 10 == 0) {
             "0$number"
         } else number.toString()
     }*/

}