package com.ias.gsscore.network.response.myaccount

import com.google.gson.annotations.SerializedName


data class PtTest (
  @SerializedName("program_id") var programId             : String? = null,
  @SerializedName("program_title") var programTitle             : String? = null,
  @SerializedName("has_child") var hasChild             : String? = null,
  @SerializedName("testId"         ) var testId         : String? = null,
  @SerializedName("testType"       ) var testType       : String? = null,
  @SerializedName("testFeature"    ) var testFeature    : String? = null,
  @SerializedName("testTitle"      ) var testTitle      : String? = null,
  @SerializedName("testNumber"     ) var testNumber     : String? = null,
  @SerializedName("que_pdf_url"    ) var quePdfUrl      : String? = null,
  @SerializedName("ans_pdf_url"    ) var ansPdfUrl      : String? = null,
  @SerializedName("maxDuration"    ) var maxDuration    : String? = null,
  @SerializedName("maxMarks"       ) var maxMarks       : String? = null,
  @SerializedName("totalQuestions" ) var totalQuestions : String? = null,
  @SerializedName("isPaid"         ) var isPaid         : String? = null,
  @SerializedName("startDate"      ) var startDate      : String? = null,
  @SerializedName("endDate"        ) var endDate        : String? = null,
  @SerializedName("resultId"       ) var resultId       : String? = null,
  @SerializedName("resultStatus"   ) var resultStatus   : String? = null

)