package com.ias.gsscore.network.response.cart

import com.google.gson.annotations.SerializedName


data class CartItemListResponse (
  var status: Boolean = false,
  var error: String = "Something went wrong",
//  var message: String = "Something went wrong",
  @SerializedName("cart_items" ) var cartItems : CartItems?        = CartItems(),
  @SerializedName("whishlist_items" ) var wishlistItems : ArrayList<WishlistData> = arrayListOf(),
  @SerializedName("total_items") var totalItems:Int = 0

  )