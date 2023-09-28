package com.ias.gsscore.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.R
import com.ias.gsscore.databinding.RowAssignedListBinding

class AssignedListAdapter : RecyclerView.Adapter<AssignedListAdapter.ViewHolder>() {
    lateinit var iconName: List<String>
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_assigned_list, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemBinding!!.apply {
            tvName.text = iconName[position]
           // ivFunctionalityIcon.setImageResource(iconList[position])
        }
    }

    override fun getItemCount(): Int {
        return iconName.size
    }

    fun updateList(iconName: List<String>) {
        this.iconName = iconName
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemBinding: RowAssignedListBinding? = DataBindingUtil.bind(itemView.rootView)
    }
}