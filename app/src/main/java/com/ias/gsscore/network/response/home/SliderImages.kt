package com.ias.gsscore.network.response.home

import com.google.gson.annotations.SerializedName


data class SliderImages (

  @SerializedName("thumbnail_url" ) var thumbnailUrl : String? = null,
  @SerializedName("url"           ) var url          : String? = null

)