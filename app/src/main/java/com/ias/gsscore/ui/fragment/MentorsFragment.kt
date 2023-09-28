package com.ias.gsscore.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.ias.gsscore.R
import com.ias.gsscore.databinding.FragmentMentorsBinding
import com.ias.gsscore.network.response.MaterialResponse
import com.ias.gsscore.ui.adapter.MentorsCallRequestListAdapter
import com.ias.gsscore.ui.adapter.MentorsListAdapter

class MentorsFragment() : Fragment() {
    lateinit var binding: FragmentMentorsBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var mentorListAdapter: MentorsListAdapter? = null
    private var mentorCallRequestListAdapter: MentorsCallRequestListAdapter? = null
    var mentorPostList = ArrayList<MaterialResponse>();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mentors, container, false)
        initialData()
        return binding.root
    }

    private fun initialData() {
        setMentorCallRequestAdapter()
        setMentorPostAdapter()
    }

    private fun setMentorCallRequestAdapter() {
        linearLayoutManager = LinearLayoutManager(context)
        binding.rvCallRequest.layoutManager = linearLayoutManager
        mentorPostList = getMentorCallRequestList();
        mentorCallRequestListAdapter = context?.let { MentorsCallRequestListAdapter(it, mentorPostList) }
        binding.rvCallRequest.adapter = mentorCallRequestListAdapter

    }

    private fun getMentorCallRequestList(): ArrayList<MaterialResponse> {
        var list = ArrayList<MaterialResponse>();
        list.add(MaterialResponse("Piyush", true))
        return list;
    }


    private fun setMentorPostAdapter() {
        linearLayoutManager = LinearLayoutManager(context)
        binding.rvMentorList.layoutManager = linearLayoutManager
        mentorPostList = getMentorPostList();
        mentorListAdapter = context?.let { MentorsListAdapter(it, mentorPostList) }
        binding.rvMentorList.adapter = mentorListAdapter

    }

    @JvmName("getMentorPostList1")
    private fun getMentorPostList(): java.util.ArrayList<MaterialResponse> {
        var list = ArrayList<MaterialResponse>();
        list.add(MaterialResponse("Piyush", true))
        list.add(MaterialResponse("Shiv Mangal", true))
        list.add(MaterialResponse("Praveen Singh", true))
        list.add(MaterialResponse("Manoj K.Jha", true))
        return list
    }

}