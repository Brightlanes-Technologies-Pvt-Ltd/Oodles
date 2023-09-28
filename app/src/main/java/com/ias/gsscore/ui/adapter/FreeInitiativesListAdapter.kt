package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ias.gsscore.R
import com.ias.gsscore.databinding.RowFreeInitiativesBinding
import com.ias.gsscore.network.response.home.FreeResourcesCategories
import com.ias.gsscore.ui.activity.WebViewActivity
import com.ias.gsscore.utils.Preferences
import com.ias.gsscore.utils.SingletonClass
import java.util.ArrayList


class FreeInitiativesListAdapter : RecyclerView.Adapter<FreeInitiativesListAdapter.ViewHolder>() {
    val list :MutableList<FreeResourcesCategories> = mutableListOf()
    var listSize = 0
    val whereFrom:String = ""
    var context:Context?=null
    var fromWhere=""


    inner class ViewHolder(val binding: RowFreeInitiativesBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowFreeInitiativesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return listSize
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.ivIcon.load(list[position].icon)
        holder.binding.tvTitle.text = list[position].title
        holder.binding.itemLayout.setOnClickListener{
            Preferences.getInstance(context).frTitle =
                list[position].title + ";" + fromWhere + ";" + list[position].id
            if (list[position].id.equals("9")){
                context?.startActivity(Intent(context, WebViewActivity::class.java).putExtra("url", "https://iasscore.in/answer-writing"))
            }else {
                SingletonClass.instance.getCustomNavController().navigate(R.id.materialFragment)
            }
        }

    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(latestCourseList: ArrayList<FreeResourcesCategories>, whereFrom:String,listSize1:Int,context:Context) {
        list.clear()
        list.addAll(latestCourseList)
        listSize = listSize1
        this.context=context
        if(whereFrom=="FreeResources"){
            fromWhere="false"

        }else{
            fromWhere="true"
        }
        notifyDataSetChanged()
    }
}