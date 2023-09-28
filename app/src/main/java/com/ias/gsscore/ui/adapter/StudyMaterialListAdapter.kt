package com.ias.gsscore.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.databinding.RowMaterialBinding
import com.ias.gsscore.network.response.MaterialResponse
import com.ias.gsscore.ui.activity.MaterialReportDetailsActivity


class StudyMaterialListAdapter(
    context1: Context,
    materialTopList1: ArrayList<MaterialResponse>
) :
        RecyclerView.Adapter<StudyMaterialListAdapter.ViewHolder>() {
    private var materialTopList = materialTopList1;
    private var context= context1;
    inner class ViewHolder(val binding: RowMaterialBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowMaterialBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return materialTopList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvName.text = materialTopList[position].nameame
        holder.binding.mainLayout.setOnClickListener {
            context.startActivity(Intent(context, MaterialReportDetailsActivity::class.java))
        }


    }
}