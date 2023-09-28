package com.ias.gsscore.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.ias.gsscore.databinding.LayoutStudyNotesDetailsPagerBinding


class
StudyNotesDetailsPagerAdapter(var context: Context, val banners: ArrayList<String>) :
    RecyclerView.Adapter<StudyNotesDetailsPagerAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: LayoutStudyNotesDetailsPagerBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutStudyNotesDetailsPagerBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)

    }

    override fun getItemCount() = banners.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        Glide.with(context).load(banners[position]).into(holder.binding.itemImage)

       // holder.binding.itemImage.setBackgroundResource(R.drawable.viewpager_img)





      /*  with(holder) {
            with(banners[position]) {
               *//* Glide.with(holder.itemView.context)
                    .load(banners[position])
                    .apply(options)
                    .into(holder.binding.itemImage)*//*

                binding.itemImage.setImageResource(R.drawable.viewpager_img)
            }
        }*/

    }



}