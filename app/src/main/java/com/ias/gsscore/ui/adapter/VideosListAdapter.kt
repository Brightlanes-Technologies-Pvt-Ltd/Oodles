package com.ias.gsscore.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.example.VideoList
import com.ias.gsscore.R
import com.ias.gsscore.databinding.RowVideosBinding
import com.ias.gsscore.network.response.myaccount.AllTestResponse
import com.ias.gsscore.ui.activity.VideoDetailsActivity


class VideosListAdapter(
    private var context: Context,
    private var materialTopList: ArrayList<VideoList>,
    private val type: String,
    private val response: AllTestResponse?,
    private val packageId: String
) :
    RecyclerView.Adapter<VideosListAdapter.ViewHolder>() {
    inner class ViewHolder(val binding: RowVideosBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowVideosBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return materialTopList.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.tvName.text = materialTopList[position].videoTitle
        holder.binding.thoughtBy.text = materialTopList[position].thoughtBy
        holder.binding.duration.text = materialTopList[position].duration
        holder.binding.tvDate.text = materialTopList[position].startDate
      //  holder.binding.tvDate.text = response!!.packageDetail!!.batchStart
        holder.binding.ivImage.load(materialTopList[position].thumbnail)
        holder.binding.videoUrl.setOnClickListener {
            if (materialTopList[position].videoUrl!=null && !materialTopList[position].videoUrl!!.contains("http")) {
                Toast.makeText(
                    context,
                    "URL not found",
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
        }
        if (type == "My Downloads")
            holder.binding.icDownloads.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.ic_delete_grey
                )
            )
        holder.binding.mainLayout.setOnClickListener {
            val intent = Intent(context,VideoDetailsActivity::class.java)
            intent.putExtra("whereFrom",type)
            intent.putExtra("videoId", materialTopList[position].videoId)
            intent.putExtra("packageId",packageId)
            intent.putExtra("programId",materialTopList[position].programId!!)
            intent.putExtra("title",materialTopList[position].videoTitle!!)
            intent.putExtra("PLAY_POSITION", 0)
            context.startActivity(intent)
           /* if (type == "My Downloads") {
                MyAccountTopListFragment.addFragment(
                    VideoDetailsFragment(
                        context,
                        type,
                        materialTopList[position].videoId,
                        packageId,
                        materialTopList[position].programId!!
                    ), context as FragmentActivity
                )
            } else {
                MyAccountTopListFragment.addFragment(
                    VideoDetailsFragment(
                        context,
                        type,
                        materialTopList[position].videoId,
                        packageId,
                        materialTopList[position].programId!!
                    ), context as FragmentActivity
                )
            }*/
        }


    }
}