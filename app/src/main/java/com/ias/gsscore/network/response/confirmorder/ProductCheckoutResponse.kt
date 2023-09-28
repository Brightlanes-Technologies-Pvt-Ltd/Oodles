package com.ias.gsscore.network.response.confirmorder

import com.google.gson.annotations.SerializedName


data class ProductCheckoutResponse (
  var status: Boolean = false,
  var error: String = "Something went wrong",
  @SerializedName("message" ) var message : Message?        = Message()
 )