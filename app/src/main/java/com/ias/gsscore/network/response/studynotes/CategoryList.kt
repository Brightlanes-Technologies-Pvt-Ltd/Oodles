package com.ias.gsscore.network.response.studynotes

import com.google.gson.annotations.SerializedName


data class CategoryList (

  @SerializedName("id"    ) var id    : String? = null,
  @SerializedName("title" ) var title : String? = null,
  var bool:Boolean = false

)