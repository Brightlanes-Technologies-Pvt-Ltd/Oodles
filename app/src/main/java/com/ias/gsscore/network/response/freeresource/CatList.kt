package com.ias.gsscore.network.response.freeresource

import com.google.gson.annotations.SerializedName


data class CatList (

  @SerializedName("cat_title" ) var cat_title : String,
  @SerializedName("cat_id" ) var cat_id : String
)