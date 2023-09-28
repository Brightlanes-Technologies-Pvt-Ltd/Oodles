package com.ias.gsscore.network.response.testresult

import com.google.gson.annotations.SerializedName


data class Score (

  @SerializedName("negativeScore" ) var negativeScore : String? = null,
  @SerializedName("trueScore"     ) var trueScore     : Int?    = null,
  @SerializedName("myScore"       ) var myScore       : String? = null

)