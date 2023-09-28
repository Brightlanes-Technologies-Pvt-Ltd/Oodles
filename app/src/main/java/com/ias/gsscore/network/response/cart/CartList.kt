package com.ias.gsscore.network.response.cart

import com.google.gson.annotations.SerializedName


data class CartList (
  @SerializedName("id"             ) var id            : String? = null,
  @SerializedName("title"          ) var title         : String? = null,
  @SerializedName("mode"           ) var mode          : String? = null,
  @SerializedName("amount"         ) var amount        : Double?    = null,
  @SerializedName("gst_amount"     ) var gstAmount     : Double?    = null,
  @SerializedName("coupon_amount"  ) var couponAmount  : Double?    = null,
  @SerializedName("payable_amount" ) var payableAmount : Double?    = null,
  @SerializedName("banner_img") var bannerImage : String? = "",

)