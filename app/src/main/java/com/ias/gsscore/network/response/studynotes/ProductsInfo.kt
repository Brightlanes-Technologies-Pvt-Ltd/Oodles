package com.ias.gsscore.network.response.studynotes

import com.google.gson.annotations.SerializedName


data class ProductsInfo (

  @SerializedName("product_details"  ) var productDetails  : ProductDetails?            = ProductDetails(),
  @SerializedName("product_Infos"    ) var productInfos    : ArrayList<String>          = arrayListOf(),
  @SerializedName("related_products" ) var relatedProducts : ArrayList<ProductList> = arrayListOf()

)