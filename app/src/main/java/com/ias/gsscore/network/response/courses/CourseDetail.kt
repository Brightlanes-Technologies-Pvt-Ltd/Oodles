package com.ias.gsscore.network.response.courses

import com.google.gson.annotations.SerializedName


data class CourseDetail (

  @SerializedName("package_code"           ) var packageCode          : String? = null,
  @SerializedName("course_title"           ) var courseTitle          : String? = null,
  @SerializedName("course_desc"            ) var courseDesc           : String? = null,
  @SerializedName("course_img"             ) var courseImg            : String? = null,
  @SerializedName("course_banner_img"      ) var courseBannerImg      : String? = null,
  @SerializedName("package_onine_amount"   ) var packageOnineAmount   : String? = null,
  @SerializedName("package_offline_amount" ) var packageOfflineAmount : String? = null,
  @SerializedName("package_batch_name"     ) var packageBatchName     : String? = null,
  @SerializedName("batchStartDate"         ) var batchStartDate       : String? = null,
  @SerializedName("batchTime"              ) var batchTime            : String? = null,
  @SerializedName("package_brochure_url"   ) var packageBrochureUrl   : String? = null,
  @SerializedName("course_youtube_id"      ) var courseYoutubeId      : String? = null,
  @SerializedName("offlineGST"             ) var offlineGST           : String? = null,
  @SerializedName("onlineGST"              ) var onlineGST            : String? = null,
  @SerializedName("classMode"              ) var classMode            : String? = null,
  @SerializedName("admission_type"         ) var admissionType        : String? = null,
  @SerializedName("marketing_number"        ) var marketingNumber        : String? = null


)