package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.R
import com.ias.gsscore.databinding.RowTopMaterialBinding
import com.ias.gsscore.network.response.studynotes.CategoryList


class StudyNotesCategoryAdapter() : RecyclerView.Adapter<StudyNotesCategoryAdapter.ViewHolder>() {
    private var clickListener: ClickListener?=null
    var materialTopList: MutableList<CategoryList> = arrayListOf()
    inner class ViewHolder(val binding: RowTopMaterialBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowTopMaterialBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return materialTopList.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            holder.binding.tvName.text = materialTopList[position].title
        holder.binding.layoutButton.setOnClickListener {
            clickListener!!.onClick(position)
        }
        if (materialTopList[position].bool) {
            holder.binding.layoutButton.setBackgroundResource(R.drawable.button_bg_white)
            holder.binding.tvName.setTextColor(Color.parseColor("#000000"))
        } else {
            holder.binding.layoutButton.setBackgroundResource(R.drawable.button_bg_light_blue)
            holder.binding.tvName.setTextColor(Color.parseColor("#FFFFFF"))
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(postList: List<CategoryList>, clickListener: ClickListener) {
        this.clickListener = clickListener
        materialTopList.clear()
        materialTopList.addAll(postList)
        notifyDataSetChanged()
    }

   public interface ClickListener{
        fun onClick(position:Int)
    }
}