package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.graphics.PorterDuff
import android.os.Build
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.R
import com.ias.gsscore.databinding.RowOptionsListBinding
import com.ias.gsscore.network.response.testresult.Questions
import com.ias.gsscore.utils.Helpers

class ResultOptionListAdapter(
    private val optionList: List<String>,
    private val questionData: Questions,
    private val context: Context
) : RecyclerView.Adapter<ResultOptionListAdapter.ViewHolder>() {
    private val otherStrings = arrayOf("A.", "B.", "C.", "D.", "E.", "F.")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = RowOptionsListBinding.inflate(LayoutInflater.from(context), parent, false)
        return ViewHolder(binding)
    }

    @RequiresApi(Build.VERSION_CODES.N)
    @SuppressLint("NotifyDataSetChanged")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.itemBinding!!.apply {
//            Helpers.setWebViewText(tvName,optionList[position])
            tvName.text = Html.fromHtml(optionList[position], Html.FROM_HTML_MODE_LEGACY)
            tvOption.text = otherStrings[position]
            ivRight.visibility=View.GONE
            if (questionData.markedOption!!.toInt()==0){
                itemLayout.background =
                    ContextCompat.getDrawable(context, R.drawable.curve_grey_stroke_dark)
            }else{
                if ((questionData.correctAnswer!!.toInt() == questionData.markedOption!!.toInt()) && (position + 1) == questionData.correctAnswer!!.toInt()) {
                    itemLayout.background =
                        ContextCompat.getDrawable(context, R.drawable.curve_green_stroke)
                } else if (questionData.correctAnswer!!.toInt() == (position + 1)) {
                    itemLayout.background =
                        ContextCompat.getDrawable(context, R.drawable.curve_green_stroke)
                }else if (questionData.markedOption!!.toInt() == (position + 1)) {
                    itemLayout.background =
                        ContextCompat.getDrawable(context, R.drawable.curve_maroon_stroke)
                    ivRight.visibility=View.VISIBLE
                    var colorCode = "#AF4C4C"
                    layoutWrongOption.background.setColorFilter(
                        Color.parseColor(colorCode), PorterDuff.Mode.SRC_ATOP
                    )
                } else {
                    itemLayout.background =
                        ContextCompat.getDrawable(context, R.drawable.curve_grey_stroke_dark)
                }
            }

        }
    }

    override fun getItemCount(): Int {
        return optionList.size
    }

    class ViewHolder(var itemBinding: RowOptionsListBinding) :
        RecyclerView.ViewHolder(itemBinding.root)
}