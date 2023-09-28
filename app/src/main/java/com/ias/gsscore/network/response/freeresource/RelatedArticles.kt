package com.ias.gsscore.network.response.freeresource

import com.google.gson.annotations.SerializedName


data class RelatedArticles (

  @SerializedName("id") var id: String? = null,
  @SerializedName("title") var title: String? = null,
)