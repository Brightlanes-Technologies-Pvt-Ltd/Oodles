package com.ias.gsscore.network.response.studynotes

import com.google.gson.annotations.SerializedName


data class ProductDetails (

  @SerializedName("id"         ) var id         : String? = null,
  @SerializedName("title"      ) var title      : String? = null,
  @SerializedName("image_url"  ) var imageUrl   : String? = null,
  @SerializedName("amount"     ) var amount     : String? = null,
  @SerializedName("highlights" ) var highlights : String? = null

)