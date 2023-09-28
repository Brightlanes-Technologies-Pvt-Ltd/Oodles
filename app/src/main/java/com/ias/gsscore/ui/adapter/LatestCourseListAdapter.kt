package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.R
import com.ias.gsscore.databinding.LayoutLatestCourseListBinding
import com.ias.gsscore.network.response.home.LatestCourses
import com.ias.gsscore.utils.SingletonClass


class LatestCourseListAdapter :
        RecyclerView.Adapter<LatestCourseListAdapter.ViewHolder>() {
    val list :MutableList<LatestCourses> = mutableListOf()
    lateinit var context : Context
    private var listSize = 0
    private var colorIndex:Int = 0
    private val colorArray = intArrayOf(R.color.green, R.color.blue_text, R.color.color_light_green, R.color.red)
    private val colorStringArray = arrayOf<String>("#4CAF50","#005DA8", "#27AC8D", "#E43B4E")
    inner class ViewHolder(val binding: LayoutLatestCourseListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            LayoutLatestCourseListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return listSize

    }

    @SuppressLint("UseCompatLoadingForColorStateLists")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvCourseTitle.text = list[position].courseTitle
        holder.binding.tvCourseTitle.setTextColor(colorArray[colorIndex])
        holder.binding.tvCourseTitle.setTextColor(Color.parseColor(colorStringArray[colorIndex]))
        holder.binding.mainLayout.backgroundTintList = context.resources.getColorStateList(colorArray[colorIndex])
        holder.binding.secondLayout.backgroundTintList = context.resources.getColorStateList(colorArray[colorIndex])
        holder.binding.viewDetails.backgroundTintList = context.resources.getColorStateList(colorArray[colorIndex])
        colorIndex++
        if (colorIndex == 4) {
            colorIndex = 0
        }
        holder.binding.viewDetails.setOnClickListener {
            val bundle = bundleOf(
                Pair("id" , list[position].courseId),
                Pair("title" ,list[position].courseTitle)
            )
            SingletonClass.instance.getCustomNavController()
                .navigate(R.id.courseDetailsFragment, bundle)
        }

        holder.binding.tvCourseTitle.setOnClickListener {
            val bundle = bundleOf(
                Pair("id" , list[position].courseId),
                Pair("title" ,list[position].courseTitle)
            )
            SingletonClass.instance.getCustomNavController()
                .navigate(R.id.courseDetailsFragment, bundle)
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(latestCourseList: ArrayList<LatestCourses>,listSize1:Int,context:Context) {
        this.context = context
        list.clear()
        list.addAll(latestCourseList)
        listSize = listSize1
        notifyDataSetChanged()
    }
}