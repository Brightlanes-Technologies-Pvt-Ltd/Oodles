package com.ias.gsscore.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.databinding.*
import com.ias.gsscore.network.response.MaterialResponse


class ScheduleChildListAdapter(
    context1: Context,
    materialTopList1: ArrayList<MaterialResponse>
) :
        RecyclerView.Adapter<ScheduleChildListAdapter.ViewHolder>() {
    private var materialTopList = materialTopList1;
    private var context= context1;
    inner class ViewHolder(val binding: RowScheduleChildBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowScheduleChildBinding.inflate(LayoutInflater.from(parent.context), parent, false)

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


    private fun getScheduleChildList(): java.util.ArrayList<MaterialResponse> {
        var list = ArrayList<MaterialResponse>();
        list.add(MaterialResponse("10:30 AM", true))
        list.add(MaterialResponse("12:30 PM", true))
        list.add(MaterialResponse("04:30 PM", true))
        return list;
    }
}