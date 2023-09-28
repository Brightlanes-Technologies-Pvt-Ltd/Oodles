package com.ias.gsscore.network.response.myaccount

import com.google.gson.annotations.SerializedName


data class VideoDetails (

  @SerializedName("video_id"    )     var video_id          : String? = null,
  @SerializedName("program_id"  )     var programId   : String? = null,
  @SerializedName("title"  )          var title   : String? = null,
  @SerializedName("tought_by"   )     var toughtBy    : String? = null,
  @SerializedName("description" )     var description : String? = null,
  @SerializedName("duration"    )     var duration    : String? = null,
  @SerializedName("video_type"  )     var videoType   : String? = null,
  @SerializedName("video_url"   )     var videoUrl    : String? = null,
  @SerializedName("thumbnail"   )     var thumbnail   : String? = null,
  @SerializedName("video_Url_Id")     var videoUrlId   : String? = null,
  @SerializedName("startDate"   )     var startDate   : String? = null,
  @SerializedName("zoom_meeting_id")  var zoomMeetingId   : String? = null,
  @SerializedName("meeting_pwd"   )   var zoomMeetingPwd   : String? = null

)