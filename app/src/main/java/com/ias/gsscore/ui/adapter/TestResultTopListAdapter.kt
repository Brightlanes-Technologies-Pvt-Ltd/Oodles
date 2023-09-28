package com.ias.gsscore.ui.adapter

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.R
import com.ias.gsscore.databinding.RowTopTestResultBinding
import com.ias.gsscore.network.response.MaterialResponse

class TestResultTopListAdapter(
    clickListener1: ClickListener?,
    materialTopList1: ArrayList<MaterialResponse>
) :
        RecyclerView.Adapter<TestResultTopListAdapter.ViewHolder>() {
    private var materialTopList = materialTopList1;
    private var clickListener = clickListener1;

    inner class ViewHolder(val binding: RowTopTestResultBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowTopTestResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return materialTopList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvName.text = materialTopList[position].nameame
        holder.binding.layoutButton.setOnClickListener {
            clickListener!!.onClick(position)
        }
        if (materialTopList[position].bool) {
            holder.binding.layoutButton.setBackgroundResource(R.drawable.round_blue_stroke)
            holder.binding.tvName.setTextColor(Color.parseColor("#005DA8"))
        } else {
            holder.binding.layoutButton.setBackgroundResource(R.drawable.round_grey_stroke_dark)
            holder.binding.tvName.setTextColor(Color.parseColor("#333333"))
        }
    }

   public interface ClickListener{
        fun onClick(position:Int)
    }
}