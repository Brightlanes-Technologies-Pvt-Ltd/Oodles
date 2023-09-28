package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ias.gsscore.R
import com.ias.gsscore.databinding.RowGsScoreStudyMaterialsBinding
import com.ias.gsscore.network.response.home.StudyMaterialList
import com.ias.gsscore.ui.activity.MainActivity
import com.ias.gsscore.utils.SingletonClass
import java.util.ArrayList


class StudyMaterialsListAdapter : RecyclerView.Adapter<StudyMaterialsListAdapter.ViewHolder>() {
    var context: Context? = null
    val list :MutableList<StudyMaterialList> = mutableListOf()
    var listSize = 0
    val whereFrom:String = ""

    inner class ViewHolder(val binding: RowGsScoreStudyMaterialsBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowGsScoreStudyMaterialsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return listSize
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.ivIcon.load(list[position].imageUrl)
        holder.binding.tvTitle.text = list[position].title
        holder.binding.itemLayout.setOnClickListener {
            val bundle = bundleOf(
                Pair("productId" , list[position].id),
                Pair("title" , list[position].title)
            )
            (context as MainActivity).viewModel.isSelected.set(2)
            SingletonClass.instance.getCustomNavController().navigate(R.id.studyNotesDetailsFragment,bundle)
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(latestCourseList: ArrayList<StudyMaterialList>,listSize1:Int) {
        list.clear()
        list.addAll(latestCourseList)
        listSize = listSize1
        notifyDataSetChanged()
    }
}