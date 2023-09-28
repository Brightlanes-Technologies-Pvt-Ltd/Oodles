package com.ias.gsscore.network.response.courses

import com.google.gson.annotations.SerializedName


data class BatchesList (

  @SerializedName("packageId"      ) var packageId      : String? = null,
  @SerializedName("classMode"      ) var classMode      : String? = null,
  @SerializedName("packageTitle"   ) var packageTitle   : String? = null,
  @SerializedName("batchName"      ) var batchName      : String? = null,
  @SerializedName("onlineAmount"   ) var onlineAmount   : String? = null,
  @SerializedName("onlineGST"      ) var onlineGST      : String? = null,
  @SerializedName("offlineAmount"  ) var offlineAmount  : String? = null,
  @SerializedName("batchStartDate" ) var batchStartDate : String? = null,
  @SerializedName("batchTime"      ) var batchTime      : String? = null,
  @SerializedName("offlineGST"     ) var offlineGST     : String? = null,
  @SerializedName("brochureURL"    ) var brochureURL    : String? = null,
  @SerializedName("imageURL"       ) var imageURL       : String? = null,
  @SerializedName("slug"           ) var slug           : String? = null,
  @SerializedName("courseType"     ) var courseType     : String? = null,
  @SerializedName("admission_type" ) var admissionType  : String? = null

)