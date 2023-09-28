package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ias.gsscore.R
import com.ias.gsscore.databinding.LayoutStudyNotesBinding
import com.ias.gsscore.network.response.studynotes.ProductList
import com.ias.gsscore.utils.SingletonClass


class StudyNotesProductAdapter(val context: Context,
                               private var packageList: List<ProductList>, var whereFrom:String ) :
        RecyclerView.Adapter<StudyNotesProductAdapter.ViewHolder>() {
    //val list: MutableList<ProductList> = mutableListOf()
   // var whereFrom:String = ""
    inner class ViewHolder(val binding: LayoutStudyNotesBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            LayoutStudyNotesBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return packageList.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            img.load(packageList[position].imageUrl)
            name.text = packageList[position].title
            if (whereFrom=="productDetails")
                discountedPrice.text = "₹${packageList[position].onlineAmount}"
            else
                discountedPrice.text = "₹${packageList[position].amount}"
           itemLayout.setOnClickListener {
                val bundle = bundleOf(
                    Pair("productId" , packageList[position].id),
                    Pair("title" , packageList[position].title)
                )
                SingletonClass.instance.getCustomNavController().navigate(R.id.studyNotesDetailsFragment,bundle)
            }
        }
    }

   /* @SuppressLint("NotifyDataSetChanged")
    fun update(postList: List<ProductList>,whereFrom:String) {
        this.whereFrom =whereFrom
        list.clear()
        list.addAll(postList)
        notifyDataSetChanged()
    }*/
}