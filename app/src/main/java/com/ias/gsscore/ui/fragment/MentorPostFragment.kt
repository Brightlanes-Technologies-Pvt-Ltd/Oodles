package com.ias.gsscore.ui.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.ias.gsscore.R
import com.ias.gsscore.databinding.FragmentMentorPostBinding
import com.ias.gsscore.network.response.MaterialResponse
import com.ias.gsscore.ui.adapter.MentorPostListAdapter

class MentorPostFragment() : Fragment() {
    lateinit var binding: FragmentMentorPostBinding
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var mentorListAdapter: MentorPostListAdapter? = null
    private var mentorPostList = ArrayList<MaterialResponse>();
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_mentor_post, container, false)
        initialData()
        return binding.root
    }

    private fun initialData() {
        linearLayoutManager = GridLayoutManager(context,2)
        binding.rvRecyclerView.layoutManager = linearLayoutManager
        setMentorPostAdapter()
    }


    private fun setMentorPostAdapter() {
        mentorPostList = getMentorPostList();
        mentorListAdapter = context?.let { MentorPostListAdapter(it, mentorPostList) }
        binding.rvRecyclerView.adapter = mentorListAdapter

    }

    @JvmName("getMentorPostList1")
    private fun getMentorPostList(): java.util.ArrayList<MaterialResponse> {
        var list = ArrayList<MaterialResponse>();
        list.add(MaterialResponse("How to Prepare", true))
        list.add(MaterialResponse("Current Affairs of 2021", true))
        return list;
    }

}