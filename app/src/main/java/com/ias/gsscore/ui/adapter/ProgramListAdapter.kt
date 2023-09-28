package com.ias.gsscore.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.databinding.RowProgramListBinding
import com.ias.gsscore.network.response.myaccount.ProgramList
import com.ias.gsscore.utils.InterfaceClickListener

class ProgramListAdapter(
    val context: Context,
    private var packageList: List<ProgramList>,
    private val type: String,
    private val clickListener: InterfaceClickListener
) :
    RecyclerView.Adapter<ProgramListAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: RowProgramListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowProgramListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return packageList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            tvName.text = packageList[position].programTitle
            tvPackageCode.visibility = View.GONE
            /* Glide.with(context)
            .load(packageList[position].imageUrl)
            .into(holder.binding.imageView)*/
            favLayout.setOnClickListener {
                clickListener.onClick(packageList[position].programId!!,type)
            }
        }
    }
}