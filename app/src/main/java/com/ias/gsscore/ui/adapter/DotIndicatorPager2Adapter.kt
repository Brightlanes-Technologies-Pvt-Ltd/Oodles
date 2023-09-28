package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.ias.gsscore.R
import com.ias.gsscore.databinding.LayoutCarouselBinding
import com.ias.gsscore.network.response.home.SliderImages
import com.ias.gsscore.utils.SingletonClass
import java.util.ArrayList


class DotIndicatorPager2Adapter() :
    RecyclerView.Adapter<DotIndicatorPager2Adapter.ViewHolder>() {
    val list: MutableList<SliderImages> = mutableListOf()

    inner class ViewHolder(val binding: LayoutCarouselBinding) :
        RecyclerView.ViewHolder(binding.root)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            LayoutCarouselBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)

    }

    override fun getItemCount() = list.size


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
//        holder.binding.itemImage.setBackgroundResource(R.drawable.viewpager_img)
        holder.binding.itemImage.load(list[position].thumbnailUrl)
        holder.binding.itemImage.setOnClickListener{
            SingletonClass.instance.getCustomNavController().navigate(R.id.courseFragment)
        }


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

    @SuppressLint("NotifyDataSetChanged")
    fun update(sliderList: ArrayList<SliderImages>) {
        list.clear()
        list.addAll(sliderList)
        notifyDataSetChanged()
    }


}