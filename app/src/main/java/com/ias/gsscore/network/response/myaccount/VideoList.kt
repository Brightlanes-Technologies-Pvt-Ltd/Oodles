package com.example.example

import com.google.gson.annotations.SerializedName


data class VideoList (
  @SerializedName("program_id"    ) var programId    : String? = null,
  @SerializedName("program_title" ) var programTitle : String? = null,
  @SerializedName("has_child"     ) var hasChild     : String? = null,
  @SerializedName("video_title"   ) var videoTitle   : String? = null,
  @SerializedName("videoUrl"     ) var videoUrl     : String? = null,
  @SerializedName("thumbnail"     ) var thumbnail     : String? = null,
  @SerializedName("videoId"       ) var videoId      : String? = null,
  @SerializedName("tought_by"       ) var thoughtBy      : String? = null,
 // Related Video
  @SerializedName("added_on"       ) var addedOn      : String? = null,
  @SerializedName("duration"       ) var duration      : String? = null,
  @SerializedName("startDate"       ) var startDate      : String? = null,
)