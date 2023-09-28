package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.network.response.courses.BatchesList
import com.ias.gsscore.databinding.RowBatchesListBinding


class BatchesListAdapter(
    public var context: Context,
    private val contextActivity: FragmentActivity,
    private val batchesList: ArrayList<BatchesList>,
    private val contextAdapterContext: BottomSheetClick
) :
    RecyclerView.Adapter<BatchesListAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: RowBatchesListBinding) :
        RecyclerView.ViewHolder(binding.root)

    private var selectedPos: Int = -1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowBatchesListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return batchesList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        holder.binding.apply {

            radioButton.isChecked = selectedPos==position
            radioButton.isChecked = selectedPos == position
            tvBatchName.text = batchesList[position].batchName
            tvDate.text = batchesList[position].batchStartDate
            tvTime.text = batchesList[position].batchTime
            radioButton.setOnClickListener {
                selectedPos = position
                contextAdapterContext.onCLick(position)
            }
        }
    }

    interface BottomSheetClick{
        fun onCLick(position:Int)
    }
}