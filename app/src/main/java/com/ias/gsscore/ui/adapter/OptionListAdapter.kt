package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.RelativeLayout
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.R
import com.ias.gsscore.databinding.RowOptionsListBinding
import com.ias.gsscore.network.request.QuestionRequest
import com.ias.gsscore.network.response.myaccount.OptionData


class OptionListAdapter : RecyclerView.Adapter<OptionListAdapter.ViewHolder>() {
    lateinit var optionList: List<OptionData>
    lateinit var answeredHashMap: HashMap<Int, QuestionRequest>
    var currentQuestionPos: Int = 0
    var selectedPos = -1;
    lateinit var context: Context
    private val otherStrings = arrayOf("A.", "B.", "C.", "D.", "E.", "F.")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowOptionsListBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("NotifyDataSetChanged", "ClickableViewAccessibility")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemBinding.apply {
//            Helpers.setWebViewText(tvName,optionList[position].name)
            tvName.text = Html.fromHtml(optionList[position].name, FROM_HTML_MODE_LEGACY)
            tvOption.text = otherStrings[position]
            val data: QuestionRequest = answeredHashMap[currentQuestionPos]!!
            if (data.selectedOption.isNotEmpty()) {
                selectedPos = data.selectedOption.toInt() - 1
            }
            if (selectedPos == position) {
                itemLayout.background =
                    ContextCompat.getDrawable(context, R.drawable.curve_green_stroke)
                data.selectedOption = "" + (position + 1)
                data.attemptType = "answered"
                answeredHashMap[currentQuestionPos] = data
            } else {
                itemLayout.background =
                    ContextCompat.getDrawable(context, R.drawable.curve_grey_stroke_dark)
            }
            itemLayout.setOnClickListener {
                selectedPos = position
                val data: QuestionRequest = answeredHashMap[currentQuestionPos]!!
                data.selectedOption = ""
                answeredHashMap[currentQuestionPos] = data
                notifyDataSetChanged()
            }
           /* itemLayoutClick.setOnClickListener {
                selectedPos = position
                val data: QuestionRequest = answeredHashMap[currentQuestionPos]!!
                data.selectedOption = ""
                answeredHashMap[currentQuestionPos] = data
                notifyDataSetChanged()
            }*/
            tvName.setOnTouchListener { _, event ->
                println("ppppppppp event ${event.action}")
                if (event.action == MotionEvent.ACTION_MOVE || event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_DOWN) {
                    selectedPos = position
                    val data: QuestionRequest = answeredHashMap[currentQuestionPos]!!
                    data.selectedOption = ""
                    answeredHashMap[currentQuestionPos] = data
                    notifyDataSetChanged()
                }
                false
            }
        }
    }

    private fun updateSelectedItem(position: Int) {
        this.notifyItemChanged(position)
    }

    override fun getItemCount(): Int {
        return optionList.size
    }

    fun updateList(
        context: Context,
        iconName: List<OptionData>,
        answeredHashMap: HashMap<Int, QuestionRequest>,
        currentQuestionPos: Int
    ) {
        this.context = context
        this.optionList = iconName
        this.answeredHashMap = answeredHashMap
        this.currentQuestionPos = currentQuestionPos
    }

    @SuppressLint("NotifyDataSetChanged")
    fun clearAnswer() {
        selectedPos = -1
        notifyDataSetChanged()
    }

    class ViewHolder(var itemBinding: RowOptionsListBinding) :
        RecyclerView.ViewHolder(itemBinding.root)

}