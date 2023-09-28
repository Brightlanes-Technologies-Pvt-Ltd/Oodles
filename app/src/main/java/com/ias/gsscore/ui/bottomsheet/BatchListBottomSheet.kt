package com.ias.gsscore.ui.bottomsheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.ias.gsscore.network.response.courses.BatchesList
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ias.gsscore.R
import com.ias.gsscore.databinding.BottomsheetBatchListBinding
import com.ias.gsscore.ui.adapter.BatchesListAdapter

class BatchListBottomSheet(
    private val contextActivity: FragmentActivity,
    private val batchesList: ArrayList<BatchesList>,
    private val interfaceContex: BottomSheetClick
) : BottomSheetDialogFragment(),
    BatchesListAdapter.BottomSheetClick {
    lateinit var binding: BottomsheetBatchListBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var adapter: BatchesListAdapter? = null
    private var selectedPos = -1
    lateinit var contextAdapterContext:BatchesListAdapter.BottomSheetClick
    companion object {
        const val TAG = "BottomSheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_batch_list, container, false)
        contextAdapterContext = this
        initView()
        return binding.root
    }

    private fun initView() {
        binding.bottomLayout.visibility = View.GONE
        linearLayoutManager = LinearLayoutManager(context)
        binding.rvQuestionPanel.layoutManager = linearLayoutManager
        adapter = context?.let { BatchesListAdapter(it,contextActivity,batchesList,contextAdapterContext) }
        binding.rvQuestionPanel.adapter = adapter
        binding.btContinue.setOnClickListener {
            interfaceContex.onCLick(selectedPos)
        }
    }

    override fun onCLick(position: Int) {
        selectedPos = position
        binding.bottomLayout.visibility = View.VISIBLE
        binding.tvDate.text = "Start From "+batchesList[selectedPos].batchStartDate
        binding.tvTime.text = batchesList[selectedPos].batchTime
    }

    interface BottomSheetClick{
        fun onCLick(position:Int)
    }
}