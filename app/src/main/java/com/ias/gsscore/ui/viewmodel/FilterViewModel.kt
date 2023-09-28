package com.ias.gsscore.ui.viewmodel

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.ias.gsscore.databinding.ActivityCourseFilterBinding
import com.ias.gsscore.network.response.courses.Category
import com.ias.gsscore.network.response.courses.Filter
import com.ias.gsscore.ui.adapter.CourseFilterChildAdapter
import com.ias.gsscore.ui.adapter.CourseFilterParentAdapter
import com.ias.gsscore.utils.SingletonClass

class FilterViewModel(binding: ViewDataBinding) : ViewModel() {
    val binding = binding as ActivityCourseFilterBinding
    lateinit var clickParentInterface : CourseFilterParentAdapter.OnClickParentInterface
    lateinit var clickChildInterface : CourseFilterChildAdapter.OnClickChildInterface
    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context
    var filterData = Filter()
    var parentList: ArrayList<Filter> = arrayListOf()
    var courseFilterParentAdapter = CourseFilterParentAdapter()
    var childList: ArrayList<Category> = arrayListOf()
    var courseFilterChildAdapter = CourseFilterChildAdapter()

    fun clearAll() {
        (context as Activity).finish()
    }

    fun funApply(){
        SingletonClass.instance.setFilterList(parentList)
        (context as Activity).finish()
    }

    fun filterData(filter: Filter) {
        filterData = filter
        if (SingletonClass.instance.getFilterList().size>0){
           parentList.addAll(SingletonClass.instance.getFilterList())
        }else {
            if (filterData.category.size > 0) {
                val data = Filter()
                data.title = "Category"
                data.categoryList.addAll(filterData.category)
                parentList.add(data)
            }
            if (filterData.mode.size > 0) {
                val data = Filter()
                data.title = "Mode"
                data.categoryList.addAll(filterData.mode)
                parentList.add(data)
            }
            if (filterData.startOn.size > 0) {
                val data = Filter()
                data.title = "Start On"
                data.categoryList.addAll(filterData.startOn)
                parentList.add(data)
            }
            if (filterData.courseType.size > 0) {
                val data = Filter()
                data.title = "Course Type"
                data.categoryList.addAll(filterData.courseType)
                parentList.add(data)
            }
        }
        if (parentList.size > 0) {
            childList.addAll(parentList[0].categoryList)
        }
        courseFilterParentAdapter.update(parentList,clickParentInterface,context)
        courseFilterChildAdapter.update(childList,clickChildInterface,context,"clearFilter",0)
    }

    fun funParentClick(position: Int) {
        courseFilterChildAdapter.update(
            parentList[position].categoryList,
            clickChildInterface,
            context,
            "clearFilter",
            position
        )
    }

    fun funChildClick(parentPosition:Int,position: Int) {
        parentList[parentPosition].categoryList[position].isSelected = !parentList[parentPosition].categoryList[position].isSelected
        courseFilterChildAdapter.notifyItemChanged(position)
    }

}