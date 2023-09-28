package com.ias.gsscore.network.response.freeresource

import com.google.gson.annotations.SerializedName


data class FreeResourceDetails (

  @SerializedName("id" ) var id      : String? = null,
  @SerializedName("title" ) var title      : String? = null,
  @SerializedName("description" ) var description      : String? = null,
  @SerializedName("file_url" ) var file_url      : String? = null,
  @SerializedName("meta_image" ) var meta_image      : String? = null,
  @SerializedName("banner_url" ) var banner_url      : String? = null,
  @SerializedName("type_name" ) var type_name      : String? = null,
  @SerializedName("category" ) var category      : String? = null,
  @SerializedName("postedOn" ) var postedOn      : String? = null,
  @SerializedName("related_articles" ) var related_articles  : ArrayList<FreeResourceList> = arrayListOf(),
  @SerializedName("video_code" ) var video_code      : String? = null




)