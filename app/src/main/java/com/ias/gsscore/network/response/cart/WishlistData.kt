package com.ias.gsscore.network.response.cart

import com.google.gson.annotations.SerializedName


data class WishlistData (
  @SerializedName("user_id"             ) var userId            : String? = null,
  @SerializedName("product_id"          ) var productId         : String? = null,
  @SerializedName("mode"           ) var mode          : String? = null,
  @SerializedName("package_title"         ) var packageTitle        : String?    = null,
  @SerializedName("package_img"     ) var packageImg     : String?    = null,
  @SerializedName("online_amount"  ) var onlineAmount  : String?    = null,
  @SerializedName("offline_amount" ) var offlineAmount : String?    = null,
  @SerializedName("offline_gst" ) var offlineGst : String?    = null,
  @SerializedName("online_gst" ) var onlineGst : String?    = null,

)