package com.ias.gsscore.network.response.myaccount

import com.example.example.PackageProgramDetails
import com.example.example.VideoList
import com.google.gson.annotations.SerializedName
import com.ias.gsscore.network.response.OodlesApiResponse


data class VideoDetailsResponse (
  @SerializedName("package_program_details" ) var packageProgramDetails : ArrayList<PackageProgramDetails> = arrayListOf(),
  @SerializedName("video_details") var videoDetails:VideoDetails,
  @SerializedName("Related_Videos") var relatedVideo: ArrayList<VideoList> = arrayListOf()



):OodlesApiResponse()