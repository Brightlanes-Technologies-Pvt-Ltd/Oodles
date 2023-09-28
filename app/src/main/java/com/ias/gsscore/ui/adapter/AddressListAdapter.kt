package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.network.response.address.AddressList
import com.ias.gsscore.databinding.LayoutAddressListBinding


class AddressListAdapter() : RecyclerView.Adapter<AddressListAdapter.ViewHolder>() {
    val list :MutableList<AddressList> = mutableListOf()
    private lateinit var interfaceContext:OnClickAddressInterface
    inner class ViewHolder(val binding: LayoutAddressListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            LayoutAddressListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return list.size

    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(postList: List<AddressList>, context: Context) {
        interfaceContext = context as OnClickAddressInterface
        list.clear()
        list.addAll(postList)
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.binding.tvName.text = data.fullname
        if (data.defaultAddress=="0"){
            holder.binding.btRadio.isChecked = false
            holder.binding.tvDefaultAddress.visibility = View.GONE
        }else{
            holder.binding.btRadio.isChecked = true
            holder.binding.tvDefaultAddress.visibility = View.VISIBLE
        }
        holder.binding.tvAddress.text = data.address
        holder.binding.tvCity.text = data.city
        holder.binding.tvState.text = "${data.stateName} , ${data.zipCode}"
        holder.binding.tvMobile.text = "Mobile: ${data.mobile}"
        holder.binding.btRemove.setOnClickListener {
            interfaceContext.onClick(data.id!!,position,"RemoveAddress")
        }
        holder.binding.btEditAddress.setOnClickListener {
            interfaceContext.onClick(data.id!!,position,"EditAddress")
        }
        holder.binding.btRadio.setOnClickListener {
            interfaceContext.onClick(data.id!!,position,"RadioButton")
        }

       /* if( holder.binding.btRadio.isChecked){
            interfaceContext.onClick(data.id!!,position,"RadioButton")
        }*/
    }

    interface OnClickAddressInterface{
        fun onClick(productId: String,position:Int,whereFrom:String)
    }
}