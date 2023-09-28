package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.R
import com.ias.gsscore.databinding.RowFeedbackFormBinding
import com.ias.gsscore.network.response.myaccount.FeedbackData

class FeedbackListAdapter : RecyclerView.Adapter<FeedbackListAdapter.ViewHolder>() {
    lateinit var rankList: List<FeedbackData>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_feedback_form, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n", "NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemBinding!!.apply {
            val data = rankList[position];
            tvName.text = data.name
            when (data.id) {
                1 -> {
                    radioBelowAverage.isChecked = true
                    radioAverage.isChecked = false
                    radioGood.isChecked = false
                    radioExcellent.isChecked = false
                }
                2 -> {
                    radioBelowAverage.isChecked = false
                    radioAverage.isChecked = true
                    radioGood.isChecked = false
                    radioExcellent.isChecked = false
                }
                3 -> {
                    radioBelowAverage.isChecked = false
                    radioAverage.isChecked = false
                    radioGood.isChecked = true
                    radioExcellent.isChecked = false
                }
                4 -> {
                    radioBelowAverage.isChecked = false
                    radioAverage.isChecked = false
                    radioGood.isChecked = false
                    radioExcellent.isChecked = true
                }
                else -> {
                    radioBelowAverage.isChecked = false
                    radioAverage.isChecked = false
                    radioGood.isChecked = false
                    radioExcellent.isChecked = false
                }
            }
            radioBelowAverage.setOnClickListener {
                rankList[position].id = 1
                notifyItemChanged(position)
            }
            radioAverage.setOnClickListener {
                rankList[position].id = 2
                notifyItemChanged(position)
            }
            radioGood.setOnClickListener {
                rankList[position].id = 3
                notifyItemChanged(position)
            }
            radioExcellent.setOnClickListener {
                rankList[position].id = 4
                notifyItemChanged(position)
            }
        }
    }

    override fun getItemCount(): Int {
        return rankList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(rankList: List<FeedbackData>) {
        this.rankList = rankList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemBinding: RowFeedbackFormBinding? = DataBindingUtil.bind(itemView.rootView)
    }
}