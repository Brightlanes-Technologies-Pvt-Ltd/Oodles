package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ias.gsscore.R
import com.ias.gsscore.databinding.LayoutStudyNotesBinding
import com.ias.gsscore.network.response.studynotes.ProductList
import com.ias.gsscore.utils.SingletonClass


class StudyNotesProductDetAdapter() :
        RecyclerView.Adapter<StudyNotesProductDetAdapter.ViewHolder>() {
    val list: MutableList<ProductList> = mutableListOf()
    var whereFrom:String = ""
    inner class ViewHolder(val binding: LayoutStudyNotesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            LayoutStudyNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return list.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            img.load(list[position].imageUrl)
            name.text = list[position].title
            if (whereFrom=="productDetails")
                discountedPrice.text = "₹${list[position].onlineAmount}"
            else
                discountedPrice.text = "₹${list[position].amount}"
           itemLayout.setOnClickListener {
                val bundle = bundleOf(
                    Pair("productId" , list[position].id),
                    Pair("title" , list[position].title)
                )
                SingletonClass.instance.getCustomNavController().navigate(R.id.studyNotesDetailsFragment,bundle)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(postList: List<ProductList>,whereFrom:String) {
        this.whereFrom =whereFrom
        list.clear()
        list.addAll(postList)
        notifyDataSetChanged()
    }
}