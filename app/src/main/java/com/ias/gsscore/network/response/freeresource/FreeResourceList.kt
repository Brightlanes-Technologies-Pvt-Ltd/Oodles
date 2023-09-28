package com.ias.gsscore.network.response.freeresource

import com.google.gson.annotations.SerializedName


data class FreeResourceList(

    @SerializedName("id") var id: String? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("description") var description: String? = null,
    @SerializedName("postedOn") var postedOn: String? = null,
    @SerializedName("catTitle") var catTitle: String? = null,
    @SerializedName("catId") var catId: String? = null,
    @SerializedName("type") var type: String? = null,
    @SerializedName("file_url") var file_url: String? = null,
    @SerializedName("image_url") var image_url: String? = null,

    @SerializedName("testType") var testType: String,
    @SerializedName("examId") var examId: String,
    @SerializedName("testLevel") var testLevel: String,
    @SerializedName("slug") var slug: String,
    @SerializedName("duration") var duration: String,
    @SerializedName("maxMarks") var maxMarks: String,
    @SerializedName("totalQuestions") var totalQuestions: String,
    @SerializedName("totalAttempts") var totalAttempts: String,
    @SerializedName("resultId") var resultId: String,
    @SerializedName("resultStatus") var resultStatus: String






)