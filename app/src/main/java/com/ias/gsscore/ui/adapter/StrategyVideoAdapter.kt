package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ias.gsscore.databinding.RowStrategyVideoBinding
import com.ias.gsscore.network.response.freeresource.FreeResourceList
import com.ias.gsscore.ui.activity.StrategyVideoDetails


class StrategyVideoAdapter(
    context1: Context,
    materialTopList1: List<FreeResourceList>
) :
        RecyclerView.Adapter<StrategyVideoAdapter.ViewHolder>() {
    private var materialTopList = materialTopList1;
    private var context=context1


    inner class ViewHolder(val binding: RowStrategyVideoBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowStrategyVideoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return materialTopList.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvName.text= materialTopList[position].title!!
        if(materialTopList[position].image_url!=""){
            Glide.with(context).load(materialTopList[position].image_url).into(holder.binding.ivImage)
        }

        holder.binding.mainLayout.setOnClickListener{
            var intent = Intent(context, StrategyVideoDetails::class.java)
            intent.putExtra("post_id",materialTopList[position].id)
            intent.putExtra("type_id",materialTopList[position].type)
            context.startActivity(intent)

        }

    }
}