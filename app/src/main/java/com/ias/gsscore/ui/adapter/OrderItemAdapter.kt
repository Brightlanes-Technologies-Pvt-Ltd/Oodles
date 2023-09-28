package com.ias.gsscore.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.databinding.LayoutOrderItemBinding


class OrderItemAdapter() :
        RecyclerView.Adapter<OrderItemAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LayoutOrderItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            LayoutOrderItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return 3

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {



    }
}