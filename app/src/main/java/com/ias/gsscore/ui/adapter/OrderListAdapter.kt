package com.ias.gsscore.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.databinding.LayoutOrderListBinding
import com.ias.gsscore.ui.activity.OrderDetailsActivity


class OrderListAdapter(var context:Context) :
        RecyclerView.Adapter<OrderListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LayoutOrderListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            LayoutOrderListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return 3

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.binding.orderLayout.setOnClickListener {
            context.startActivity(Intent(context,OrderDetailsActivity::class.java))
        }



    }
}