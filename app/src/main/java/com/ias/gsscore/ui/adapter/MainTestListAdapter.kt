package com.ias.gsscore.ui.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.DownloadBrochureInterface
import com.ias.gsscore.R
import com.ias.gsscore.databinding.RowMainTestsBinding
import com.ias.gsscore.network.response.myaccount.MainsTest
import com.ias.gsscore.ui.activity.MainTestRankDetailsActivity
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Multipart

import retrofit2.http.POST
import retrofit2.http.Part


class MainTestListAdapter(
    val context: Context,
    val list: ArrayList<MainsTest>,
    private val downloadBrochureInterface:DownloadBrochureInterface,
    private val uploadAnswerInterface : UploadAnswerInterface
) :
    RecyclerView.Adapter<MainTestListAdapter.ViewHolder>() {

    inner class ViewHolder(val binding: RowMainTestsBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

        val binding =
            RowMainTestsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return ViewHolder(binding)
    }

    override fun getItemCount(): Int {
        return list.size

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.binding.apply {
            tvName.text = list[position].title
            if (list[position].discussionVideo == null) {
                llVideo.visibility = View.GONE
            } else {
                llVideo.visibility = View.VISIBLE
            }
            if (list[position].copyType.let { it.equals("submited") }) {
                tvUploadAnswer.text = "View Copy"
            } else if (list[position].copyType.let { it.equals("to_be_submit") }) {
                tvUploadAnswer.text = "Upload Copy"
            } else if (list[position].copyType.let { it.equals("submited_and_evaluated") }) {
                tvUploadAnswer.text = "Evaluated Copy"
            }
            llVideo.background =
                ContextCompat.getDrawable(context, R.drawable.ic_rectangle_green)
//            ivVideo.visibility = View.VISIBLE
            btQuestionPaper.setOnClickListener {
                /* val intent = Intent(context, WebViewActivity::class.java)
                 intent.putExtra("title", list[position].title)
                 intent.putExtra("url", list[position].questionPdfUrl)
                 context.startActivity(intent)*/
                // Helpers.downloadPdf(context, list[position].questionPdfUrl, list[position].title)
                downloadBrochureInterface.downLoadFile(
                    list[position].questionPdfUrl!!,
                    list[position].title!!,
                    position
                )
            }
            downloadHint.setOnClickListener {
                /*  val intent = Intent(context, WebViewActivity::class.java)
                  intent.putExtra("title", list[position].title)
                  intent.putExtra("url", list[position].answerPdfUrl)
                  context.startActivity(intent)*/
//                  Helpers.downloadPdf(context, list[position].answerPdfUrl,  list[position].title)
                downloadBrochureInterface.downLoadFile(
                    list[position].answerPdfUrl!!,
                    list[position].title!!,
                    position
                )
            }
            ivMore.setOnClickListener {
                val intent = Intent(context, MainTestRankDetailsActivity::class.java)
                intent.putExtra("title", list[position].title)
                intent.putExtra("testId", list[position].testId)
                intent.putExtra("hasDiscussionVideo", list[position].hasDiscussionVideo)
                intent.putExtra("breakup", list[position].breakup)
                intent.putExtra("whereFrom", "TopperRank")
                context.startActivity(intent)
            }
            llVideo.setOnClickListener {
                val intent = Intent(context, MainTestRankDetailsActivity::class.java)
                intent.putExtra("title", list[position].title)
                intent.putExtra("testId", list[position].testId)
                intent.putExtra("hasDiscussionVideo", list[position].hasDiscussionVideo)
                intent.putExtra("breakup", list[position].breakup)
                intent.putExtra("whereFrom", "pdfIcon")
                context.startActivity(intent)
            }
            uploadAnswer.setOnClickListener {
                if(tvUploadAnswer.text.toString().equals("View Copy", true)){
                    uploadAnswerInterface.viewUploadedCopy(list[position].userCopy)
                }else if(tvUploadAnswer.text.toString().equals("Evaluated Copy",true)){
                    uploadAnswerInterface.viewEvaluatedCopy(list[position].evaluatedCopy)
                }else if(tvUploadAnswer.text.toString().equals("Upload Copy", true)) {
                    uploadAnswerInterface.uploadAnswer(list[position])
                }
            }
        }
    }

    interface UploadAnswerInterface{
        fun uploadAnswer(data:MainsTest)
        fun viewUploadedCopy(uploadedPdf : String?)
        fun viewEvaluatedCopy(evaluatedPdf : String?)
    }
}