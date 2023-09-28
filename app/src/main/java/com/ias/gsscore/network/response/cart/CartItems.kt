package com.ias.gsscore.network.response.cart

import com.google.gson.annotations.SerializedName


data class CartItems (
  @SerializedName("total_items"          ) var totalItems         : Int?            = null,
  @SerializedName("total_amount"         ) var totalAmount        : Double?         = null,
  @SerializedName("total_coupon_amount"  ) var totalCouponAmount  : Int?            = null,
  @SerializedName("total_payable_amount" ) var totalPayableAmount : Int?            = null,
  @SerializedName("list"                 ) var list               : ArrayList<CartList> = arrayListOf()


)