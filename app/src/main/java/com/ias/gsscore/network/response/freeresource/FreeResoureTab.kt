package com.ias.gsscore.network.response.freeresource

import com.google.gson.annotations.SerializedName


data class FreeResoureTab (

  @SerializedName("id" ) var id      : String? = null,
  @SerializedName("title" ) var title      : String? = null,
  @SerializedName("icon" ) var icon      : String? = null,
  var bool:Boolean
)