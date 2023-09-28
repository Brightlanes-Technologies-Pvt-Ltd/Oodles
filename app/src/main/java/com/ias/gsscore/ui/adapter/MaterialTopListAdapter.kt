package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.R
import com.ias.gsscore.databinding.RowTopMaterialBinding
import com.ias.gsscore.network.response.myaccount.PackageHasResponse
import java.util.*
import kotlin.collections.ArrayList


class MaterialTopListAdapter(
    clickListener1: ClickListener?,
    materialTopList1: ArrayList<PackageHasResponse>
) :
        RecyclerView.Adapter<MaterialTopListAdapter.ViewHolder>() {
    private var materialTopList = materialTopList1;
    private var clickListener = clickListener1;

    inner class ViewHolder(val binding: RowTopMaterialBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowTopMaterialBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return materialTopList.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val s  = materialTopList[position].nameame.replace("-"," ")
            .replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        val sr = s.split(" ")
        if(sr.size>1)
        holder.binding.tvName.text =sr[0] +" "+ sr[1].replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString() }
        else
            holder.binding.tvName.text =sr[0]
        holder.binding.layoutButton.setOnClickListener {
            clickListener!!.onClick(position)
        }
        if (materialTopList[position].bool) {
            holder.binding.layoutButton.setBackgroundResource(R.drawable.button_bg_white)
            holder.binding.tvName.setTextColor(Color.parseColor("#000000"))
        } else {
            holder.binding.layoutButton.setBackgroundResource(R.drawable.button_bg_light_blue)
            holder.binding.tvName.setTextColor(Color.parseColor("#FFFFFF"))
        }
    }

   public interface ClickListener{
        fun onClick(position:Int)
    }
}