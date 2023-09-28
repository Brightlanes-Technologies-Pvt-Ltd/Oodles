package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.databinding.RowQuestionPanelListBinding
import com.ias.gsscore.network.response.testresult.Questions
import com.ias.gsscore.network.response.testresult.Report
import com.ias.gsscore.ui.activity.TestResultActivity
import com.ias.gsscore.ui.fragment.ResultDetailedAnalysisFragment


class ResultQuestionPanelListAdapter(
    private val context: Context,
    private val contextActivity: FragmentActivity,
    private val questionList: ArrayList<Questions>,
    private val reportData: Report
) :
    RecyclerView.Adapter<ResultQuestionPanelListAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: RowQuestionPanelListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowQuestionPanelListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return questionList.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val questionData = questionList[position]
        holder.binding.apply {
           tvQuestionNo.text = "" + (position + 1)
           layoutMain.setOnClickListener {
               TestResultActivity.getInstance().viewModel.addFragment(
                    ResultDetailedAnalysisFragment(
                        questionList[position],
                        reportData,
                        position,
                        questionList.size
                    ), contextActivity,position,"questionPanel"
                )
            }

        /*    var colorCode = "#EFEFEF"
            when (questionData.attemptType) {
                "answered" -> {
                    colorCode = "#4CAF50"
                    holder.binding.tvQuestionNo.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )
                }
                "markForReview" -> {
                    colorCode = "#005DA8"
                    holder.binding.tvQuestionNo.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )
                }
                "notAnswered" ->{
                    colorCode = "#E43B4E"
                    holder.binding.tvQuestionNo.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.white
                        )
                    )
                }
                else -> {
                    holder.binding.tvQuestionNo.setTextColor(
                        ContextCompat.getColor(
                            context,
                            R.color.black
                        )
                    )
                }
            }
            holder.binding.layoutMain.background.setColorFilter(
                Color.parseColor(colorCode), PorterDuff.Mode.SRC_ATOP
            )*/
        }
    }
}