package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.example.PackageProgramDetails
import com.example.example.VideoList
import com.ias.gsscore.databinding.RowOtherVideosBinding
import com.ias.gsscore.ui.activity.VideoDetailsActivity

class OtherVideosListAdapter() :
        RecyclerView.Adapter<OtherVideosListAdapter.ViewHolder>() {
    val list: MutableList<VideoList> = mutableListOf()
    lateinit var context:Context
    lateinit var type:String
    lateinit var  packageProgramDetails: ArrayList<PackageProgramDetails>
    inner class ViewHolder(val binding: RowOtherVideosBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowOtherVideosBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
           return list.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvVideoTitle.text = list[position].videoTitle
        holder.binding.tvDuration.text = list[position].duration
        holder.binding.tvThoughtBy.text = list[position].thoughtBy
        holder.binding.tvAddedOn.text = list[position].addedOn
        holder.binding.ivImage.load( list[position].thumbnail)
        holder.binding.mainLayout.setOnClickListener {
            val intent = Intent(context, VideoDetailsActivity::class.java)
            intent.putExtra("whereFrom",type)
            intent.putExtra("videoId", list[position].videoId)
            intent.putExtra("packageId",packageProgramDetails[0].packageId!!)
            intent.putExtra("programId",packageProgramDetails[0].programId!!)
            intent.putExtra("title",packageProgramDetails[0].packageTitle!!)
            intent.putExtra("PLAY_POSITION", 0)
            context.startActivity(intent)
            (context as Activity).finish()
           /* MyAccountTopListFragment.addFragment(
                VideoDetailsFragment(
                    context,
                    type,
                    list[position].videoId,
                    packageProgramDetails[0].packageId!!,
                    packageProgramDetails[0].programId!!
                ), context as FragmentActivity
            )*/
        }


    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(
        listData: ArrayList<VideoList>,
        context: Context,
        type: String,
        packageProgramDetails: ArrayList<PackageProgramDetails>,
    ) {
        this.context = context
        this.type = type
        this.packageProgramDetails = packageProgramDetails
        list.clear()
        list.addAll(listData)
        notifyDataSetChanged()
    }
}