package com.ias.gsscore.network.response.home

import com.google.gson.annotations.SerializedName


data class StudyMaterialList (

  @SerializedName("id"            ) var id           : String? = null,
  @SerializedName("title"         ) var title        : String? = null,
  @SerializedName("image_url"     ) var imageUrl     : String? = null,
  @SerializedName("online_amount" ) var onlineAmount : String? = null,
  @SerializedName("online_gst"    ) var onlineGst    : String? = null

)