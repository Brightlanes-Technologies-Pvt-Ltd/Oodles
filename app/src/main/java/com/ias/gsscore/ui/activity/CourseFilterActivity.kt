package com.ias.gsscore.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.gson.Gson
import com.ias.gsscore.R
import com.ias.gsscore.databinding.ActivityCourseFilterBinding
import com.ias.gsscore.network.response.courses.Filter
import com.ias.gsscore.ui.adapter.CourseFilterChildAdapter
import com.ias.gsscore.ui.adapter.CourseFilterParentAdapter
import com.ias.gsscore.ui.viewmodel.FilterViewModel
import com.ias.gsscore.viewmodelfactory.ActivityViewModelFactory

class CourseFilterActivity : AppCompatActivity(), CourseFilterParentAdapter.OnClickParentInterface,
    CourseFilterChildAdapter.OnClickChildInterface {
    lateinit var binding: ActivityCourseFilterBinding
    lateinit var viewModel: FilterViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_course_filter)
        viewModel = ViewModelProvider(
            this,
            ActivityViewModelFactory(binding, application)
        )[FilterViewModel::class.java]
        binding.viewmodel = viewModel
        viewModel.context = this
        viewModel.clickParentInterface = this
        viewModel.clickChildInterface = this
        val filter = intent.getStringExtra("filter")
        val gson = Gson()
        viewModel.filterData(gson.fromJson(filter, Filter::class.java))

    }

    override fun onClickParent(position: Int) {
        viewModel.funParentClick(position)
    }

    override fun onClickChild(parentPosition:Int,position: Int) {
        viewModel.funChildClick(parentPosition,position)
    }

}