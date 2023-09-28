package com.ias.gsscore.network.response.myaccount

import com.google.gson.annotations.SerializedName


data class PackageDetail (

  @SerializedName("title"        ) var title       : String? = null,
  @SerializedName("package_code" ) var packageCode : String? = null,
  @SerializedName("batch_start"  ) var batchStart  : String? = null,
  @SerializedName("batch_time"   ) var batchTime   : String? = null

)