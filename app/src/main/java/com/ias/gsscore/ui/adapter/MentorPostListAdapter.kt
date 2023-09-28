package com.ias.gsscore.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.databinding.RowMentorPostBinding
import com.ias.gsscore.network.response.MaterialResponse


class MentorPostListAdapter(
    context1: Context,
    materialTopList1: ArrayList<MaterialResponse>
) :
        RecyclerView.Adapter<MentorPostListAdapter.ViewHolder>() {
    private var materialTopList = materialTopList1;
    private var context= context1;
    inner class ViewHolder(val binding: RowMentorPostBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowMentorPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return materialTopList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvName.text = materialTopList[position].nameame
      /*  holder.binding.mainLayout.setOnClickListener {
            val intent = Intent(context, MaterialReportDetailsActivity::class.java)
            context.startActivity(intent)
        }*/


    }
}