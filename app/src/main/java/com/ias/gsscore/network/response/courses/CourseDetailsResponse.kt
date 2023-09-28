package com.ias.gsscore.network.response.courses

import com.google.gson.annotations.SerializedName
import com.ias.gsscore.network.response.OodlesApiResponse


data class CourseDetailsResponse (

  @SerializedName("course_info" ) var courseInfo : CourseInfo? = CourseInfo(),
  @SerializedName("state_list" ) var stateList  : ArrayList<Category> = arrayListOf(),

):OodlesApiResponse()