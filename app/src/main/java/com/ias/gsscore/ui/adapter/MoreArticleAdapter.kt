package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.databinding.RowMoreArticleBinding
import com.ias.gsscore.network.response.freeresource.FreeResourceList
import com.ias.gsscore.utils.Helpers
import kotlin.collections.ArrayList


class MoreArticleAdapter(
    context1: Context,
    materialTopList1: ArrayList<FreeResourceList>
) :
        RecyclerView.Adapter<MoreArticleAdapter.ViewHolder>() {
    private var materialTopList = materialTopList1;
    private var context=context1


    inner class ViewHolder(val binding: RowMoreArticleBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowMoreArticleBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return materialTopList.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.title.text= materialTopList[position].title!!
        Helpers.setWebViewText(holder.binding.desc,materialTopList[position].description!!)
    }
}