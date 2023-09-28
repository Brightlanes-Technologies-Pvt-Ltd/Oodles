package com.ias.gsscore.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.databinding.RowMainTestQuestionBinding
import com.ias.gsscore.network.response.myaccount.QuestionData
import com.ias.gsscore.ui.activity.MainTestVideoDetailActivity

class MainsTestQuestionListListAdapter(
    private val context: Context,
    private var questionList: ArrayList<QuestionData>,
    private val hasDiscussionVideo: String,
    private val breakup: String,
    private val modelAnswerInterface: ModelAnswerInterface
) :
    RecyclerView.Adapter<MainsTestQuestionListListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RowMainTestQuestionBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding = RowMainTestQuestionBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return questionList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {
            holder.binding.apply {
                tvQuestion.text = questionList[position].question
                tvWord.text = questionList[position].wordLimit
                if (questionList[position].videoId=="")
                    discussionVideo.visibility = View.GONE
                else
                    discussionVideo.visibility = View.VISIBLE

                discussionVideo.setOnClickListener {
                    val intent = Intent(context, MainTestVideoDetailActivity::class.java)
                    intent.putExtra("title", "Discussion Video")
                    intent.putExtra("testId", questionList[position].testId)
                    intent.putExtra("videoId", questionList[position].videoId)
                    context.startActivity(intent)
                }
                modelAnswer.setOnClickListener {
                    modelAnswerInterface.modelAnswerCLick(questionList[position])
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }

     interface  ModelAnswerInterface{
        fun modelAnswerCLick(data:QuestionData)
    }
}

