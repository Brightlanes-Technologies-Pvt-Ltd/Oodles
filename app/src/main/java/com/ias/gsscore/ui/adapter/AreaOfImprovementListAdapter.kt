package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.databinding.RowAreaOfImprovementBinding
import com.ias.gsscore.network.response.testresult.Topics

class AreaOfImprovementListAdapter(
     var topicList: ArrayList<Topics>
) :
        RecyclerView.Adapter<AreaOfImprovementListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RowAreaOfImprovementBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowAreaOfImprovementBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return topicList.size

    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(areaOfImprovementList: ArrayList<Topics>) {
        topicList = areaOfImprovementList
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvName.text = topicList[position].title
        holder.binding.tvNeedImprove.text = "Need ${topicList[position].progressPercentage}% Improvement"
        holder.binding.progressBar.progress = topicList[position].progressPercentage
        println(""+topicList[position].progressPercentage)

    }
}