package com.ias.gsscore.network.response.myaccount

import com.google.gson.annotations.SerializedName


data class MainsTest (
  @SerializedName("program_id")             var programId             : String? = null,
  @SerializedName("program_title")          var programTitle             : String? = null,
  @SerializedName("has_child")             var hasChild             : String? = null,
  @SerializedName("test_id"              ) var testId             : String? = null,
  @SerializedName("title"                ) var title              : String? = null,
  @SerializedName("start_date"           ) var startDate          : String? = null,
  @SerializedName("copy_upload_till"     ) var copyUploadTill     : String? = null,
  @SerializedName("question_pdf_url"     ) var questionPdfUrl     : String? = null,
  @SerializedName("toppers_pdf_url"      ) var toppersPdfUrl      : String? = null,
  @SerializedName("answer_pdf_url"       ) var answerPdfUrl       : String? = null,
  @SerializedName("discussion_video"     ) var discussionVideo    : String? = null,
  @SerializedName("show_leaderboard"     ) var showLeaderboard    : String? = null,
  @SerializedName("breakup"              ) var breakup            : String? = null,
  @SerializedName("has_discussion_video" ) var hasDiscussionVideo : String? = null,
  @SerializedName("copy_type" )            var copyType           : String? = null,
  @SerializedName("user_copy" )            var userCopy           : String? = null,
  @SerializedName("evaluated_copy" )       var evaluatedCopy           : String? = null
)