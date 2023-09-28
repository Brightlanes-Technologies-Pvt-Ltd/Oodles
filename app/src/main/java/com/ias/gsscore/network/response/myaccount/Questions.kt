package com.ias.gsscore.network.response.myaccount

import com.google.gson.annotations.SerializedName


data class Questions (

  @SerializedName("id"          ) var id          : String? = null,
  @SerializedName("instruction" ) var instruction : String? = null,
  @SerializedName("question"    ) var question    : String? = null,
  @SerializedName("option1"     ) var option1     : String? = null,
  @SerializedName("option2"     ) var option2     : String? = null,
  @SerializedName("option3"     ) var option3     : String? = null,
  @SerializedName("option4"     ) var option4     : String? = null,
  @SerializedName("option5"     ) var option5     : String? = null,
  @SerializedName("correct_ans" ) var correctAns  : String? = null

)