package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.databinding.LayoutDigitalItemBinding
import com.ias.gsscore.network.response.cart.CartItems


class CartDigitalItemAdapter() :
        RecyclerView.Adapter<CartDigitalItemAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LayoutDigitalItemBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            LayoutDigitalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return 1

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(postList: List<CartItems>) {
       // list = postList
        notifyDataSetChanged()
    }
}