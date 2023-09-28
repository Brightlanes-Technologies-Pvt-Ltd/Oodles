package com.ias.gsscore.network.response.testresult

import com.google.gson.annotations.SerializedName


data class Attempt (

  @SerializedName("fluke"       ) var fluke       : Int? = null,
  @SerializedName("sillyError"  ) var sillyError  : Int? = null,
  @SerializedName("perfect"     ) var perfect     : Int? = null,
  @SerializedName("good"        ) var good        : Int? = null,
  @SerializedName("overtime"    ) var overtime    : Int? = null,
  @SerializedName("waste"       ) var waste       : Int? = null,
  @SerializedName("correct"     ) var correct     : Int? = null,
  @SerializedName("incorrect"   ) var incorrect   : Int? = null,
  @SerializedName("unattempted" ) var unattempted : Int? = null

)