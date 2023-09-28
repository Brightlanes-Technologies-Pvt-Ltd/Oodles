package com.ias.gsscore.network.response.myaccount

import com.google.gson.annotations.SerializedName

/**
 * @author AK 17-05-2023
 */
data class TopperResult (
  @SerializedName("questionId"            ) var questionId            : String? = null,
  @SerializedName("solvingTime"           ) var solvingTime           : String? = null,
  @SerializedName("markedOption"          ) var markedOption          : String? = null,
)