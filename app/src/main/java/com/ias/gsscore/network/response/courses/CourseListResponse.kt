package com.ias.gsscore.network.response.courses

import com.google.gson.annotations.SerializedName
import com.ias.gsscore.network.response.OodlesApiResponse
import com.ias.gsscore.network.response.home.LatestCourses


data class CourseListResponse (
  @SerializedName("logo"        ) var logo       : String?               = null,
  @SerializedName("filter"      ) var filter     : Filter               = Filter(),
  @SerializedName("course_list" ) var courseList : ArrayList<CourseList> = arrayListOf(),
  @SerializedName("latest_courses" ) var latestCoursesList : ArrayList<LatestCourses> = arrayListOf()

):OodlesApiResponse()