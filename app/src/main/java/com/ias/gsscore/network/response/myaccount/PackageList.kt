package com.ias.gsscore.network.response.myaccount

import com.google.gson.annotations.SerializedName


data class PackageList (

  @SerializedName("id"               ) var id              : String? = null,
  @SerializedName("title"            ) var title           : String? = null,
  @SerializedName("package_code"     ) var packageCode     : String? = null,
  @SerializedName("mode"             ) var mode            : String? = null,
  @SerializedName("course_type"      ) var courseType      : String? = null,
  @SerializedName("product_type"     ) var productType     : String? = null,
  @SerializedName("product_category" ) var productCategory : String? = null,
  @SerializedName("batch_start"      ) var batchStart      : String? = null,
  @SerializedName("package_has"      ) var packageHas      : String? = null

)