package com.ias.gsscore.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.ViewModel
import com.ias.gsscore.R
import com.ias.gsscore.databinding.ActivityTestResultBinding
import com.ias.gsscore.network.response.testresult.Report
import com.ias.gsscore.ui.activity.FeedbackFormActivity
import com.ias.gsscore.ui.bottomsheet.ResultQuestionPanelBottomSheet

class TestResultViewModel(binding: ViewDataBinding) : ViewModel() {
    var binding = binding as ActivityTestResultBinding
    private var resultQuestionPanelBottomSheet: ResultQuestionPanelBottomSheet? = null
    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context
    lateinit var supportFragmentManager: FragmentManager
    lateinit var testId: String
    lateinit var programId: String
    lateinit var reportData: Report
    var currentQuestionPos: Int = 0
    fun clickBack() {
        (context as Activity).finish()
    }

    fun clickGiveFeedback() {
        val intent = Intent(context, FeedbackFormActivity::class.java)
        intent.putExtra("testId", testId)
        intent.putExtra("programId", programId)
        context.startActivity(intent)
    }

    fun clickQuestionPdf() {
        Toast.makeText(
            context,
            "Question PDF",
            Toast.LENGTH_SHORT
        )
            .show()
    }

    fun clickSolutionPdf() {
        Toast.makeText(
            context,
            "Solution PDF",
            Toast.LENGTH_SHORT
        )
            .show()
    }

    fun swipeToOpenQuestion() {
        resultQuestionPanelBottomSheet =
            ResultQuestionPanelBottomSheet(context as FragmentActivity, reportData).apply {
                show(supportFragmentManager, ResultQuestionPanelBottomSheet.TAG)
            }
    }

    fun addFragment(fragment: Fragment?, requireActivity: FragmentActivity, currentQuestionPos:Int,whereFrom:String) {
        if (whereFrom == "questionPanel")
            this.currentQuestionPos = currentQuestionPos
        if (fragment == null) return
        requireActivity.supportFragmentManager.beginTransaction()
            .replace(R.id.rootContainer, fragment)
            .commitAllowingStateLoss()

        try {
            resultQuestionPanelBottomSheet!!.dismiss()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

}