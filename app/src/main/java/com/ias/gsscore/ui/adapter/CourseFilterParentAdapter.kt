package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.R
import com.ias.gsscore.databinding.LayoutItemFilterParentBinding
import com.ias.gsscore.network.response.courses.Filter
import java.util.ArrayList


class CourseFilterParentAdapter() :
        RecyclerView.Adapter<CourseFilterParentAdapter.ViewHolder>() {
    val list :MutableList<Filter> = mutableListOf()
    lateinit var clickParentInterface : OnClickParentInterface
    lateinit var context: Context
    private var selectedPos = 0
    inner class ViewHolder(val binding: LayoutItemFilterParentBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutItemFilterParentBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvTitle.text=list[position].title
        if (selectedPos == position)
            holder.binding.tvTitle.setTextColor(context.resources.getColor(R.color.blue_text))
        else
            holder.binding.tvTitle.setTextColor(context.resources.getColor(R.color.text_color_333333))
        holder.binding.filterParent.setOnClickListener{
            selectedPos = position
            clickParentInterface.onClickParent(position)
            notifyDataSetChanged()
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    fun update(
        listData: ArrayList<Filter>,
        clickParentInterface: OnClickParentInterface,
        context: Context,
    ) {
        this.context = context
        this.clickParentInterface = clickParentInterface
        list.clear()
        list.addAll(listData)
        notifyDataSetChanged()
    }

    interface OnClickParentInterface{
        fun onClickParent(position: Int)
    }

}