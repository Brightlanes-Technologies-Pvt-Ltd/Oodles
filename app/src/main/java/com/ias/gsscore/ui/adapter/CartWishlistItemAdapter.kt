package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ias.gsscore.R
import com.ias.gsscore.databinding.RowWishlistItemBinding
import com.ias.gsscore.network.response.cart.WishlistData


class CartWishlistItemAdapter() :
        RecyclerView.Adapter<CartWishlistItemAdapter.ViewHolder>() {
    val list: MutableList<WishlistData> = mutableListOf()
    lateinit var removeWishlistItemInterface: RemoveWishlistItemInterface
    inner class ViewHolder(val binding: RowWishlistItemBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowWishlistItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return list.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            tvTitle.text = list!![position].packageTitle
            tvAmount.text = "₹ "+ list!![position].onlineAmount
            tvDeliverBy.text = "Delivery By : "
           packageImage.load(list!![position].packageImg) {
                fallback(R.drawable.book1)
                error(R.drawable.book1)
                placeholder(R.drawable.book1)
            }
            removeCartItem.setOnClickListener {
                removeWishlistItemInterface.removeWishlistItem(list[position].productId!!,position)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(postList: List<WishlistData>, removeCartItemInterface: RemoveWishlistItemInterface) {
        this.removeWishlistItemInterface = removeCartItemInterface
        list.clear()
        list.addAll(postList)
        notifyDataSetChanged()
    }

    fun removeAndUpdateList(position:Int){
        list.remove(list[position])
        notifyDataSetChanged()
    }

    interface RemoveWishlistItemInterface{
        fun removeWishlistItem(productId: String,position:Int)
    }
}