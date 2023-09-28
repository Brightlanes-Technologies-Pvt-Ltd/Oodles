package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ias.gsscore.databinding.LayoutProductDetailsListBinding
import com.ias.gsscore.network.response.cart.CartList


class ProductDetailAdapter() : RecyclerView.Adapter<ProductDetailAdapter.ViewHolder>() {
    val list :MutableList<CartList> = mutableListOf()
    private var context : Context? = null
    private var productType : String? = null
    private lateinit var interfaceContext:OnClickAddressInterface
    inner class ViewHolder(val binding: LayoutProductDetailsListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            LayoutProductDetailsListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return list.size

    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(postList: List<CartList>, context: Context, productType : String) {
        this.productType = productType
        this.context = context
        list.clear()
        list.addAll(postList)
        notifyDataSetChanged()
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = list[position]
        holder.binding.tvName.text = data.title
        holder.binding.tvProductType.text = "Product Type : $productType"
        holder.binding.tvPrice.text = "Rs "+data.amount
        Glide.with(context!!).load(data.bannerImage)
            .into(holder.binding.packageImage)

//        holder.binding.btRadio.setOnClickListener {
//            interfaceContext.onClick(data.id!!,position,"RadioButton")
//        }

       /* if( holder.binding.btRadio.isChecked){
            interfaceContext.onClick(data.id!!,position,"RadioButton")
        }*/
    }

    interface OnClickAddressInterface{
        fun onClick(productId: String,position:Int,whereFrom:String)
    }
}