package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.DownloadBrochureInterface
import com.ias.gsscore.R
import com.ias.gsscore.databinding.LayoutCourseListBinding
import com.ias.gsscore.network.response.courses.CourseList
import com.ias.gsscore.utils.SingletonClass


class CourseListAdapter() :
    RecyclerView.Adapter<CourseListAdapter.ViewHolder>() {
    private lateinit var context:Context
    private val courseList :MutableList<CourseList> = mutableListOf()
    inner class ViewHolder(val binding: LayoutCourseListBinding) :
        RecyclerView.ViewHolder(binding.root)
    lateinit var downloadBrochureInterface : DownloadBrochureInterface

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            LayoutCourseListBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return courseList.size

    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            val data = courseList[position]
            tvTitle.text = data.title
           // tvAmount.text = "₹ ${data.onlineAmount!!.toFloat()+((data.onlineAmount!!.toFloat()*data.online_gst!!.toFloat())/100)}"
            tvAmount.text= "Batch Start - ${data.batchStart}"
            if (data.brochureUrl.toString().contains("http"))
                btBrochure.visibility = View.VISIBLE
            else
                btBrochure.visibility = View.GONE
            topLayout.setOnClickListener {
                val bundle = bundleOf(
                    Pair("id" , data.courseId),
                    Pair("title" , data.title)
                )
                SingletonClass.instance.getCustomNavController()
                    .navigate(R.id.courseDetailsFragment, bundle)
            }

            btBrochure.setOnClickListener {
               /* val intent = Intent(context, WebViewActivity::class.java)
                intent.putExtra("title", data.title)
                intent.putExtra("url", data.brochureUrl)
                context.startActivity(intent)*/
               // downloadPdf(context,data.brochureUrl,data.title)
                downloadBrochureInterface.downLoadFile(data.brochureUrl!!,data.title!!,position)
            }
            batchDetailsLayout.setOnClickListener {
                val bundle = bundleOf(
                    Pair("id" , data.courseId),
                    Pair("title" , data.title)
                )
                SingletonClass.instance.getCustomNavController()
                    .navigate(R.id.courseDetailsFragment, bundle)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun update(latestCourseList: ArrayList<CourseList>,context:Context,downloadBrochureInterface:DownloadBrochureInterface) {
        this.downloadBrochureInterface = downloadBrochureInterface
        this.context = context
        courseList.clear()
        courseList.addAll(latestCourseList)
        notifyDataSetChanged()
    }

}