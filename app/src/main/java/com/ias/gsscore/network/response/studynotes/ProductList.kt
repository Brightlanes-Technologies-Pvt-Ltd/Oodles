package com.ias.gsscore.network.response.studynotes

import com.google.gson.annotations.SerializedName


data class ProductList (

  @SerializedName("id"        ) var id       : String? = null,
  @SerializedName("title"     ) var title    : String? = null,
  @SerializedName("image_url" ) var imageUrl : String? = null,
  @SerializedName("amount"    ) var amount   : String? = null,

  // Product Details
  @SerializedName("online_amount" ) var onlineAmount : String? = null,
@SerializedName("online_gst"    ) var onlineGst    : String? = null



)