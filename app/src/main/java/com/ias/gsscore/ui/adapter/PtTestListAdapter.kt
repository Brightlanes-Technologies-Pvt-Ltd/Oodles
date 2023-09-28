package com.ias.gsscore.ui.adapter

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.R
import com.ias.gsscore.databinding.RowPtTestBinding
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.response.OodlesApiResponse
import com.ias.gsscore.network.response.myaccount.PtTest
import com.ias.gsscore.ui.activity.TakeTestActivity
import com.ias.gsscore.ui.activity.TestResultActivity
import com.ias.gsscore.utils.Preferences
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch


class PtTestListAdapter(
    private val context: Context,
    private var materialTopList: ArrayList<PtTest>,
    private val coroutineScope: CoroutineScope,
    private val apiService: ApiInterface,
    private val loadingDialog: AlertDialog
) :
        RecyclerView.Adapter<PtTestListAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: RowPtTestBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowPtTestBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return materialTopList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvName.text = materialTopList[position].testTitle
        holder.binding.totalQuestions.text = materialTopList[position].totalQuestions+" Questions"
        holder.binding.maxDuration.text = materialTopList[position].maxDuration+" Mint."
        holder.binding.maxMarks.text = materialTopList[position].maxMarks+" Max Score"
        if (materialTopList[position].resultStatus==null || materialTopList[position].resultStatus.equals("0")){
            holder.binding.tvTakeTest.text = "Take Test"
        }else{
            holder.binding.tvTakeTest.text = "View Result"
        }

        holder.binding.takeTest.setOnClickListener {
            if (materialTopList[position].resultStatus==null || materialTopList[position].resultStatus.equals("0"))
            testInstructionApiCall(materialTopList[position].testId!!,materialTopList[position].testTitle!!,materialTopList[position].programId!!)
            else{
                val intent = Intent(context, TestResultActivity::class.java)
                intent.putExtra("testId",materialTopList[position].testId)
                intent.putExtra("programId",materialTopList[position].programId)
                intent.putExtra("resultId",materialTopList[position].resultId)
                context.startActivity(intent) }
        }
    }

    private fun dialogTakeTest(
        title: String?,
        testId: String?,
        response: OodlesApiResponse,
        programId: String
    ) {
        try {
            val dialog = Dialog(context)
          // dialog.window!!.requestFeature(1)
            dialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            dialog.setContentView(R.layout.dialog_test_instruction)
            dialog.setCancelable(false)
            val layoutContinue = dialog.findViewById<LinearLayout>(R.id.layoutContinue)
            val layoutClose = dialog.findViewById<LinearLayout>(R.id.layoutClose)
            val tvTitle = dialog.findViewById<TextView>(R.id.tvTitle)
            val instruction = dialog.findViewById<TextView>(R.id.tvInstruction)
            tvTitle.text = title
            instruction.text = response.instructions
            layoutClose.setOnClickListener { dialog.dismiss() }
            layoutContinue.setOnClickListener {
                dialog.dismiss()
                val intent = Intent(context, TakeTestActivity::class.java)
                intent.putExtra("title",title)
                intent.putExtra("testId",testId)
                intent.putExtra("programId",programId)
                context.startActivity(intent)
            }
            dialog.show()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun testInstructionApiCall(testId: String, testTitle: String, programId: String) {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        request["test_id"] = testId
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.getTestInstructions(request)
            val response: OodlesApiResponse = result.body()!!
            if (response.status) {
                dialogTakeTest(testTitle,testId,response,programId)
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
}