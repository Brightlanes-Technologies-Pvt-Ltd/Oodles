package com.ias.gsscore.ui.bottomsheet

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.ias.gsscore.network.response.courses.BatchesList
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.ias.gsscore.R
import com.ias.gsscore.databinding.BottomsheetCourseTypeBinding

class CourseTypeBottomSheet(
    private val batchesData: BatchesList,
    private val interfaceCourseTypeContex: BottomSheetCourseTypeClick
) : BottomSheetDialogFragment(){
    lateinit var binding: BottomsheetCourseTypeBinding
    private var selectedPos = -1
    companion object {
        const val TAG = "BottomSheet"
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.bottomsheet_course_type, container, false)
        initView()
        return binding.root
    }

    @SuppressLint("SetTextI18n")
    private fun initView() {
        binding.tvDate.text = "Start From "+batchesData.batchStartDate
        binding.tvTime.text = batchesData.batchTime
        if(batchesData.onlineGST!!.toFloat()>0){
            binding.tvOnlineAmount.text = "₹${batchesData.onlineAmount!!}(+GST)"
            binding.onlineGst.visibility=View.GONE
        }else{
            binding.tvOnlineAmount.text = "₹ ${batchesData.onlineAmount!!} "
            binding.onlineGst.visibility=View.VISIBLE
        }
        if(batchesData.offlineGST!!.toFloat()>0){
            binding.tvOfflineAmount.text = "₹${batchesData.offlineAmount!!}(+GST)"
            binding.offlineGst.visibility=View.GONE
        }else {
            binding.tvOfflineAmount.text = "₹${batchesData.offlineAmount!!} "
            binding.offlineGst.visibility=View.VISIBLE
        }

        when {
            batchesData.classMode.equals("1") -> {
                binding.layoutOnline.visibility = View.VISIBLE
                binding.layoutOffline.visibility = View.GONE
            }
            batchesData.classMode.equals("2") -> {
                binding.layoutOnline.visibility = View.GONE
                binding.layoutOffline.visibility = View.VISIBLE
            }
            else -> {
                binding.layoutOnline.visibility = View.VISIBLE
                binding.layoutOffline.visibility = View.VISIBLE
            }
        }


        binding.layoutOnline.setOnClickListener {
            selectedPos = 1
            binding.layoutOnline.background = ContextCompat.getDrawable(requireContext(),R.drawable.curve_blue_stroke)
            binding.layoutOffline.background = ContextCompat.getDrawable(requireContext(),R.drawable.ic_bg_light)
            binding.layoutAddToCart.background = ContextCompat.getDrawable(requireContext(),R.drawable.curve_blue_solid)
        }
        binding.layoutOffline.setOnClickListener {
            selectedPos = 2
            binding.layoutOffline.background = ContextCompat.getDrawable(requireContext(),R.drawable.curve_blue_stroke)
            binding.layoutOnline.background = ContextCompat.getDrawable(requireContext(),R.drawable.ic_bg_light)
            binding.layoutAddToCart.background = ContextCompat.getDrawable(requireContext(),R.drawable.curve_blue_solid)
        }
        binding.layoutAddToCart.setOnClickListener {
            if (selectedPos!=-1)
                interfaceCourseTypeContex.onClickCourseType(selectedPos,batchesData)
        }
    }

    interface BottomSheetCourseTypeClick{
        fun onClickCourseType(position:Int,batchesData: BatchesList)
    }
}