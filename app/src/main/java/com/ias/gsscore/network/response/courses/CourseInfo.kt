package com.ias.gsscore.network.response.courses

import com.google.gson.annotations.SerializedName


data class CourseInfo (
  @SerializedName("course_detail" ) var courseDetail : CourseDetail?          = CourseDetail(),
  @SerializedName("courseFaq"     ) var courseFaq    : ArrayList<CourseInfos>      = arrayListOf(),
  @SerializedName("courseInfos"   ) var courseInfos  : ArrayList<CourseInfos> = arrayListOf(),
  @SerializedName("batches_list"  ) var batchesList  : ArrayList<BatchesList> = arrayListOf()

)