package com.ias.gsscore.network.response.myaccount

import com.google.gson.annotations.SerializedName


data class QuestionData (

  @SerializedName("quesId"        ) var quesId       : String? = null,
  @SerializedName("question" ) var question : String? = null,
  @SerializedName("description"  ) var description  : String? = null,
  @SerializedName("solution"   ) var solution   : String? = null,
  @SerializedName("video_id"   ) var videoId   : String? = null,
  @SerializedName("test_id"   ) var testId   : String? = null,
  @SerializedName("wordLimit"   ) var wordLimit   : String? = null,
  //Mains Question Details
  @SerializedName("videoUrl"   ) var videoUrl   : String? = null,
  @SerializedName("videoType"   ) var videoType   : String? = null,
  @SerializedName("toughtBy"   ) var toughtBy   : String? = null,
  @SerializedName("videoDuration"   ) var videoDuration   : String? = null,
  @SerializedName("video_Url_Id"   ) var videoUrlId   : String? = null,
)