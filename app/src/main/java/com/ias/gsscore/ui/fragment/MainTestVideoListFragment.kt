package com.ias.gsscore.ui.fragment

import android.content.Context
import android.os.Build
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.databinding.DataBindingUtil
import com.ias.gsscore.R
import com.ias.gsscore.databinding.FragmentCourseTestBinding
import com.ias.gsscore.network.response.myaccount.QuestionData
import com.ias.gsscore.ui.adapter.*

class MainTestVideoListFragment(context: Context, private val relatedVideo: ArrayList<QuestionData>) : Fragment() {
    lateinit var binding: FragmentCourseTestBinding
    private var videosListAdapter: MainVideosListAdapter? = null
    @RequiresApi(Build.VERSION_CODES.N)
    override
    fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_course_test, container, false)
       // val view: View = inflater.inflate(R.layout.fragment_pt_test, container, false)
        initialData()
        return binding.root;

    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun initialData() {
        binding.tvTitle.visibility = View.GONE
        videosListAdapter = context?.let {
            MainVideosListAdapter(
                requireContext(),relatedVideo
            )
        }
        binding.rvMaterial.adapter = videosListAdapter
        if (relatedVideo.size>0){
            binding.scrollView.visibility = View.VISIBLE
            binding.tvDataNotFound.visibility = View.GONE
        }else{
            binding.scrollView.visibility = View.GONE
            binding.tvDataNotFound.visibility = View.VISIBLE
        }
    }


}
