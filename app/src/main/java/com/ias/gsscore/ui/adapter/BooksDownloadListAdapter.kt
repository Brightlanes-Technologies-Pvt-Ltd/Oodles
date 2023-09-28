package com.ias.gsscore.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.databinding.RowBooksDownloadBinding
import com.ias.gsscore.network.response.freeresource.FreeResourceList


class BooksDownloadListAdapter(
    context1: Context,
    materialTopList1: List<FreeResourceList>,private val downloadClick:DownloadClick
) :
        RecyclerView.Adapter<BooksDownloadListAdapter.ViewHolder>() {
    private var materialTopList = materialTopList1;
    private var context= context1;
    inner class ViewHolder(val binding: RowBooksDownloadBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowBooksDownloadBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return materialTopList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvName.text=materialTopList[position].title
        if(materialTopList[position].file_url!!.equals("")){
            holder.binding.download.visibility= View.GONE
        }else{
            holder.binding.download.visibility= View.VISIBLE
        }
        holder.binding.download.setOnClickListener{
            downloadClick.onDownloadClick(materialTopList[position].file_url!!,materialTopList[position].title!!)
        }

//        holder.binding.mainLayout.setOnClickListener {
//            context.startActivity(Intent(context, MaterialBlogsDetailsActivity::class.java))
//        }


    }

    interface DownloadClick{
        fun onDownloadClick(position:String,title:String)
    }


}