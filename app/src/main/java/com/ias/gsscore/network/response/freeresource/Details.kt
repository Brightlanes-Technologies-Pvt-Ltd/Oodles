package com.ias.gsscore.network.response.freeresource

import com.google.gson.annotations.SerializedName


data class Details (

  @SerializedName("details" ) var details : FreeResourceDetails? = FreeResourceDetails(),
  @SerializedName("related_articles" ) var related_articles  : ArrayList<FreeResourceList> = arrayListOf(),
  @SerializedName("more_articles" ) var more_articles  : ArrayList<FreeResourceList> = arrayListOf()
)