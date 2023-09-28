package com.ias.gsscore.network.response.courses

import com.google.gson.annotations.SerializedName


data class CourseList (

  @SerializedName("course_id"      ) var courseId      : String? = null,
  @SerializedName("title"          ) var title         : String? = null,
  @SerializedName("brochure_url"   ) var brochureUrl   : String? = null,
  @SerializedName("online_amount"  ) var onlineAmount  : String? = null,
  @SerializedName("offline_amount" ) var offlineAmount : String? = null,
  @SerializedName("batch_start"    ) var batchStart    : String? = null,
  @SerializedName("admission_type" ) var admissionType : String? = null,
  @SerializedName("online_gst" ) var online_gst : String? = null,
  @SerializedName("offline_gst" ) var offline_gst : String? = null

)