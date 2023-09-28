package com.ias.gsscore.network.response.testresult

import com.google.gson.annotations.SerializedName


data class Topics (

  @SerializedName("title"       ) var title       : String? = null,
  @SerializedName("total"       ) var total       : Int    = 0,
  @SerializedName("correct"     ) var correct     : Int   = 0,
  @SerializedName("incorrect"   ) var incorrect   : Int    = 0,
  @SerializedName("unattempted" ) var unattempted : Int    = 0,
var progressPercentage : Int = 0

)