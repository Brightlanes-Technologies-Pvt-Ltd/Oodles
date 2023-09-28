package com.ias.gsscore.network.response.home

import com.google.gson.annotations.SerializedName


data class LatestCourses (
  @SerializedName("title" ) var courseTitle : String? = null,
  @SerializedName("course_id"   ) var courseId   : String? = null,
  @SerializedName("brochure_url"   ) var brochureUrl   : String? = null,
  @SerializedName("online_amount"   ) var onlineAmount   : String? = null,
  @SerializedName("offline_amount"   ) var offlineAmount   : String? = null,
  @SerializedName("batch_start"   ) var batchStart   : String? = null,
  @SerializedName("admission_type"   ) var admissionType   : String? = null,

)