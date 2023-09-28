package com.ias.gsscore.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.databinding.RowViewAllBatchesBinding
import com.ias.gsscore.network.response.courses.BatchesList


class ViewAllBatchesListAdapter(
    context1: Context,
    materialTopList1: ArrayList<BatchesList>,  private val enrollContext: EnrollClick,private val brochureClick: BrochureClick
) :
        RecyclerView.Adapter<ViewAllBatchesListAdapter.ViewHolder>() {
    private var materialTopList = materialTopList1;
    inner class ViewHolder(val binding: RowViewAllBatchesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowViewAllBatchesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return materialTopList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvName.text = materialTopList[position].batchName
        holder.binding.bDate.text = materialTopList[position].batchStartDate
        holder.binding.bTime.text = materialTopList[position].batchTime
        if(materialTopList[position].onlineGST!!.toFloat()>0){
            holder.binding.online.text=   "Online: ₹"+materialTopList[position].onlineAmount+" (+GST)"
        }else{
            holder.binding.online.text=   "Online: ₹"+materialTopList[position].onlineAmount
            holder.binding.includeOnGst.visibility= View.VISIBLE
        }
        if(materialTopList[position].offlineGST!!.toFloat()>0){
            holder.binding.offline.text=   "Offline: ₹"+materialTopList[position].offlineAmount+" (+GST)"
        }else{
            holder.binding.offline.text=   "Offline: ₹"+materialTopList[position].offlineAmount
            holder.binding.includeOffGst.visibility= View.VISIBLE
        }

        holder.binding.enroll.setOnClickListener {
            enrollContext.onEnrollClick(position)
        }

        holder.binding.brochure.setOnClickListener{
            brochureClick.onBrochureClick(position)
        }
      /*  holder.binding.mainLayout.setOnClickListener {
            val intent = Intent(context, MaterialReportDetailsActivity::class.java)
            context.startActivity(intent)
        }*/


    }

    interface EnrollClick{
        fun onEnrollClick(position:Int)
    }

    interface BrochureClick{
        fun onBrochureClick(position:Int)
    }
}