package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.R
import com.ias.gsscore.databinding.RowQuestionPanelListBinding
import com.ias.gsscore.network.request.QuestionRequest
import com.ias.gsscore.network.response.myaccount.StartTestResponse
import com.ias.gsscore.ui.activity.TakeTestActivity
import com.ias.gsscore.ui.fragment.TestQuestionFragment


class QuestionPanelListAdapter(
    private val context: Context,
    private val answeredHashMap: HashMap<Int, QuestionRequest>,
    private val startTestResponse: StartTestResponse,
    private val contextActivity: FragmentActivity
) :
    RecyclerView.Adapter<QuestionPanelListAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: RowQuestionPanelListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowQuestionPanelListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return answeredHashMap.size
    }

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("SetTextI18n", "ResourceAsColor")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvQuestionNo.text = "" + (position + 1)
        holder.binding.layoutMain.setOnClickListener {
            val data:QuestionRequest = answeredHashMap[position]!!
            data.attemptOrder+= 1
            answeredHashMap[position] = data
            TakeTestActivity.getInstance().viewModel.addFragment(TestQuestionFragment(startTestResponse.questionsList[position],answeredHashMap,position),contextActivity,position,"questionPanel")
        }
        var colorCode = "#EFEFEF"
        when (answeredHashMap[position]!!.attemptType) {
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
        )
    }
}