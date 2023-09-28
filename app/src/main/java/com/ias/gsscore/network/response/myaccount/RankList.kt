package com.ias.gsscore.network.response.myaccount

import com.google.gson.annotations.SerializedName


data class RankList (

  @SerializedName("userId"             ) var userId             : String? = null,
  @SerializedName("userName"           ) var userName           : String? = null,
  @SerializedName("userImage"          ) var userImage          : String? = null,
  @SerializedName("gsscore"              ) var score              : String? = null,
  @SerializedName("correctQuestions"   ) var correctQuestions   : String? = null,
  @SerializedName("incorrectQuestions" ) var incorrectQuestions : String? = null,
  @SerializedName("timeTaken"          ) var timeTaken          : String? = null,
  @SerializedName("resultId"           ) var resultId           : String? = null,
  @SerializedName("accuracy"           ) var accuracy           : String? = null

)