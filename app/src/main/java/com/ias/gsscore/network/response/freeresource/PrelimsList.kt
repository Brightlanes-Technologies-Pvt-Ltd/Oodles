package com.ias.gsscore.network.response.freeresource

import com.google.gson.annotations.SerializedName


data class PrelimsList(
    @SerializedName("id") var id: String,
    @SerializedName("testType") var testType: String,
    @SerializedName("examId") var examId: String,
    @SerializedName("testLevel") var testLevel: String,
    @SerializedName("title") var title: String,
    @SerializedName("slug") var slug: String,
    @SerializedName("duration") var duration: String,
    @SerializedName("maxMarks") var maxMarks: String,
    @SerializedName("totalQuestions") var totalQuestions: String,
    @SerializedName("totalAttempts") var totalAttempts: String,
    @SerializedName("postedOn") var postedOn: String,
    @SerializedName("resultId") var resultId: String,
    @SerializedName("resultStatus") var resultStatus: String

)