package com.ias.gsscore.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.databinding.RowEbooksBinding
import com.ias.gsscore.network.response.myaccount.EbookList


class EBooksListAdapter(
    context: Context,
    private var materialTopList: ArrayList<EbookList>
) :
        RecyclerView.Adapter<EBooksListAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: RowEbooksBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowEbooksBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return materialTopList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvName.text = materialTopList[position].ebookTitle
      /*  holder.binding.mainLayout.setOnClickListener {
            val intent = Intent(context, MaterialReportDetailsActivity::class.java)
            context.startActivity(intent)
        }*/


    }
}