package com.ias.gsscore.network.response.testresult

import com.google.gson.annotations.SerializedName


data class AttemptLog (

  @SerializedName("fluke"      ) var fluke      : Int? = null,
  @SerializedName("sillyError" ) var sillyError : Int? = null,
  @SerializedName("perfect"    ) var perfect    : Int? = null,
  @SerializedName("good"       ) var good       : Int? = null,
  @SerializedName("overtime"   ) var overtime   : Int? = null,
  @SerializedName("waste"      ) var waste      : Int? = null

)