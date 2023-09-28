package com.ias.gsscore.network.response.studynotes

import com.google.gson.annotations.SerializedName
import com.ias.gsscore.network.response.OodlesApiResponse


data class ProductDetailsResponse (
  @SerializedName("products_info" ) var productsInfo : ProductsInfo? = ProductsInfo()

):OodlesApiResponse()