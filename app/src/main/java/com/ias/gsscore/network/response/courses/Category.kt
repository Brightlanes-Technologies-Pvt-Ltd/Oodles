package com.ias.gsscore.network.response.courses

import com.google.gson.annotations.SerializedName


data class Category (
  var isSelected : Boolean = false,
  @SerializedName("id"    ) var id    : String? = null,
  @SerializedName("title" ) var title : String? = null,
  @SerializedName("state_name" ) var stateName : String? = null,

)