package com.ias.gsscore.ui.viewmodel

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.databinding.ObservableInt
import androidx.databinding.ViewDataBinding
import androidx.lifecycle.ViewModel
import com.ias.gsscore.network.response.courses.Filter
import com.google.gson.Gson
import com.ias.gsscore.DownloadBrochureInterface
import com.ias.gsscore.R
import com.ias.gsscore.databinding.FragmentCourseBinding
import com.ias.gsscore.network.response.courses.CourseList
import com.ias.gsscore.network.ApiInterface
import com.ias.gsscore.network.RetrofitHelper
import com.ias.gsscore.network.response.courses.CourseListResponse
import com.ias.gsscore.network.response.home.*
import com.ias.gsscore.ui.activity.CourseFilterActivity
import com.ias.gsscore.ui.adapter.*
import com.ias.gsscore.utils.Preferences
import com.ias.gsscore.utils.SingletonClass
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class CourseFragmentViewModel(binding: ViewDataBinding) : ViewModel() {
    val binding = binding as FragmentCourseBinding
    var isVisibleCourseRv: ObservableInt = ObservableInt(0)
    var isVisibleLatestCourseRv: ObservableInt = ObservableInt(0)
    lateinit var downloadBrochureInterface : DownloadBrochureInterface
    @SuppressLint("StaticFieldLeak")
    lateinit var context: Context
    lateinit var loadingDialog: AlertDialog
    private val viewModelJob = Job()
    private val coroutineScope = CoroutineScope(Dispatchers.Main + viewModelJob)
    private val apiService = RetrofitHelper.getInstance().create(ApiInterface::class.java)
    var adapterLatestCourse = LatestCourseListAdapter()
    var latestCourseList: ArrayList<LatestCourses> = arrayListOf()
    var courseAdapter = CourseListAdapter()
    var courseList: ArrayList<CourseList> = arrayListOf()
    var filterData = Filter()

    fun clickFilter() {
        if (filterData.category.size > 0) {
            val filterString = Gson().toJson(filterData)
            val intent = Intent(context, CourseFilterActivity::class.java)
            intent.putExtra("filter", filterString)
            context.startActivity(intent)
        } else
            Toast.makeText(
                context,
                "Filter not available",
                Toast.LENGTH_SHORT
            )
                .show()

    }

    fun courseListApi(courseType: String) {
        var request: HashMap<String, String> = HashMap()
        request["user_id"] = Preferences.getInstance(context).userId
        if (courseType == "")
            request = funCreateFilterRequest(request)
        else
            request["course_type"] = courseType
        coroutineScope.launch {
            loadingDialog.show()
            val result = apiService.courseList(request)
            if (result.body() == null) {
                Toast.makeText(
                    context,
                    context.getString(R.string.error_msg),
                    Toast.LENGTH_SHORT
                )
                    .show()
                loadingDialog.dismiss()
                return@launch
            }
            val response: CourseListResponse = result.body()!!
            if (response.status) {
                //Filter
                filterData = response.filter
                //Latest Course
                latestCourseList = response.latestCoursesList
                adapterLatestCourse.update(latestCourseList, latestCourseList.size, context)
                if (latestCourseList.size > 0)
                    isVisibleLatestCourseRv.set(1)
                //Course List
                courseList = response.courseList
                courseAdapter.update(courseList, context,downloadBrochureInterface)
                if (courseList.size > 0)
                    isVisibleCourseRv.set(1)
            } else {
                Toast.makeText(
                    context,
                    response.error,
                    Toast.LENGTH_SHORT
                )
                    .show()
            }
            loadingDialog.dismiss()
        }
    }

    private fun funCreateFilterRequest(request: HashMap<String, String>): HashMap<String, String> {
        val parentList = SingletonClass.instance.getFilterList()
        if (parentList.size > 0) {
            for (parent in parentList) {
                for (child in parent.categoryList) {
                    if (child.isSelected) {
                        if (parent.title == "Category") {
                            if (request.containsKey("category"))
                                request["category"] = request["category"].toString() + "," + child.id
                            else
                                request["category"] = child.id!!
                        }
                        if (parent.title == "Mode") {
                            if (request.containsKey("mode"))
                                request["mode"] = request["mode"].toString() + "," + child.id
                            else
                                request["mode"] = child.id!!
                        }
                        if (parent.title == "Start On") {
                            if (request.containsKey("start_on"))
                                request["start_on"] = request["start_on"].toString() + "," + child.id
                            else
                                request["start_on"] = child.id!!
                        }
                        if (parent.title == "Course Type") {
                            if (request.containsKey("course_type"))
                                request["course_type"] = request["course_type"].toString() + "," + child.id
                            else
                                request["course_type"] = child.id!!
                        }
                    }

                }
            }
        }
        /*  request["category"] = ""
               request["mode"] = ""
               request["start_on"] = ""
               request["course_type"] = ""
               request["is_featured"] = ""*/

        return request
    }

}