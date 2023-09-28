package com.ias.gsscore.network.response.home

import com.google.gson.annotations.SerializedName


data class FreeResourcesCategories (

  @SerializedName("id"    ) var id    : String? = null,
  @SerializedName("title" ) var title : String? = null,
  @SerializedName("icon"  ) var icon  : String? = null

)