package com.ias.gsscore.ui.adapter

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.ias.gsscore.R
import com.ias.gsscore.databinding.RowMainTestRankListBinding
import com.ias.gsscore.network.response.myaccount.RankList
import com.ias.gsscore.network.response.myaccount.TopperData

class MainTestRankListAdapter(private val rankList: ArrayList<TopperData>) : RecyclerView.Adapter<MainTestRankListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        var view = LayoutInflater.from(parent.context)
            .inflate(R.layout.row_main_test_rank_list, parent, false)
        return ViewHolder(view)
    }

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = rankList[position]
        holder.itemBinding!!.apply {
            tvRank.text = ""+(position+1)
            tvName.text = data.userName
            tvMarks.text = data.testScore
        }
    }

    override fun getItemCount(): Int {
        return rankList.size
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateList(rankList: ArrayList<RankList>) {
       // this.rankList = rankList
        notifyDataSetChanged()
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var itemBinding: RowMainTestRankListBinding? = DataBindingUtil.bind(itemView.rootView)
    }
}