package com.ias.gsscore.ui.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.databinding.RowMainsVideosBinding
import com.ias.gsscore.network.response.myaccount.QuestionData
import com.ias.gsscore.ui.activity.MainTestVideoDetailActivity
import com.ias.gsscore.utils.Helpers


class MainVideosListAdapter(
    private var context: Context,
    private val questionData: ArrayList<QuestionData>
) :
    RecyclerView.Adapter<MainVideosListAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: RowMainsVideosBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowMainsVideosBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return questionData.size

    }

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Helpers.setWebViewText(holder.binding.tvQuestion,questionData[position].question!!)
        holder.binding.thoughtBy.text = questionData[position].toughtBy
        holder.binding.tvWords.text = questionData[position].wordLimit
        holder.binding.tvVideoDuration.text = questionData[position].videoDuration
       // holder.binding.ivImage.load(questionData[position].thumbnail)
        holder.binding.mainLayout.setOnClickListener {
            val intent = Intent(context, MainTestVideoDetailActivity::class.java)
            intent.putExtra("title", "Related Videos")
            intent.putExtra("testId", questionData[position].testId)
            intent.putExtra("videoId",questionData[position].videoId)
            context.startActivity(intent)
            (context as Activity).finish()
        }


    }
}