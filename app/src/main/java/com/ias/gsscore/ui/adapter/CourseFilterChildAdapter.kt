package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.R
import com.ias.gsscore.databinding.LayoutItemFilterChildBinding
import com.ias.gsscore.network.response.courses.Category
import java.util.ArrayList


class CourseFilterChildAdapter() :
    RecyclerView.Adapter<CourseFilterChildAdapter.ViewHolder>() {
    lateinit var context: Context
    val list: MutableList<Category> = mutableListOf()
    lateinit var clickChildInterface: OnClickChildInterface
    var parentPosition:Int = 0

    inner class ViewHolder(val binding: LayoutItemFilterChildBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutItemFilterChildBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.apply {
            if (list[position].isSelected)
                childLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.grey_light))
            else
                childLayout.setBackgroundColor(ContextCompat.getColor(context,R.color.white))


            txtChild.text = list[position].title
            childLayout.setOnClickListener {
                clickChildInterface.onClickChild(parentPosition,position)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(
        listData: ArrayList<Category>,
        clickChildInterface: OnClickChildInterface,
        context: Context,
        clearFilter: String,
        parentPosition:Int
    ) {
        this.parentPosition = parentPosition
        this.context = context
        this.clickChildInterface = clickChildInterface
        list.clear()
        list.addAll(listData)
        notifyDataSetChanged()
    }

    interface OnClickChildInterface {
        fun onClickChild(parentPosition:Int,position: Int)
    }
}