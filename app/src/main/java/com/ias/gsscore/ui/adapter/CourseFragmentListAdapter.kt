package com.ias.gsscore.ui.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.ias.gsscore.network.response.myaccount.PackageList
import com.ias.gsscore.R
import com.ias.gsscore.databinding.RowCourseListBinding
import com.ias.gsscore.utils.SingletonClass

class CourseFragmentListAdapter(
    val context: Context,
    private var packageList: List<PackageList>
) :
    RecyclerView.Adapter<CourseFragmentListAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: RowCourseListBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowCourseListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return packageList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        try {

            holder.binding.apply {
                tvName.text = packageList[position].title
                if (packageList[position].packageCode!!.isEmpty())
                    tvPackageCode.visibility=View.GONE
                else
                    tvPackageCode.visibility=View.VISIBLE
                tvPackageCode.text = packageList[position].packageCode
                /* Glide.with(context)
                .load(packageList[position].imageUrl)
                .into(holder.binding.imageView)*/
                favLayout.setOnClickListener {
                    /* val bundle = bundleOf("id" to  packageList[position].id)
                     SingletonClass.instance.getCustomNavController().navigate(R.id.courseDetailsFragment,bundle)*/
                    val gson = Gson()
                    val json = gson.toJson(packageList[position])
                    val bundle = bundleOf("data" to json)
                    SingletonClass.instance.getCustomNavController().navigate(R.id.myAccountTopListFragment,bundle)
                }
            }
        }catch (e:Exception){
            e.printStackTrace()
        }
    }
}