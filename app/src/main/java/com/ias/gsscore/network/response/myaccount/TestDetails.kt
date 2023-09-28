package com.ias.gsscore.network.response.myaccount

import com.google.gson.annotations.SerializedName


data class TestDetails (

  @SerializedName("id"              ) var id             : String? = null,
  @SerializedName("title"           ) var title          : String? = null,
  @SerializedName("exam_type"       ) var examType       : String? = null,
  @SerializedName("test_type"       ) var testType       : String? = null,
  @SerializedName("duration"        ) var duration       : String? = null,
  @SerializedName("total_questions" ) var totalQuestions : String? = null

)