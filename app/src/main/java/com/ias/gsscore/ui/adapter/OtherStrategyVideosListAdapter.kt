package com.ias.gsscore.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ias.gsscore.databinding.RowOtherVideosBinding
import com.ias.gsscore.network.response.freeresource.FreeResourceList
import com.ias.gsscore.ui.activity.StrategyVideoDetails

class OtherStrategyVideosListAdapter( context1:Context, list1: ArrayList<FreeResourceList>) :
        RecyclerView.Adapter<OtherStrategyVideosListAdapter.ViewHolder>() {
    val list=  list1
    var context = context1
    lateinit var type:String
    inner class ViewHolder(val binding: RowOtherVideosBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowOtherVideosBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return list.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvVideoTitle.text = list[position].title
        holder.binding.tvDuration.text = list[position].duration
        holder.binding.tvThoughtBy.text = list[position].catTitle
        holder.binding.tvAddedOn.text = list[position].postedOn
        holder.binding.ivImage.load( list[position].image_url)
        holder.binding.mainLayout.setOnClickListener{
            var intent = Intent(context, StrategyVideoDetails::class.java)
            intent.putExtra("post_id",list[position].id)
            intent.putExtra("type_id",list[position].type)
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(intent)

        }



    }

}