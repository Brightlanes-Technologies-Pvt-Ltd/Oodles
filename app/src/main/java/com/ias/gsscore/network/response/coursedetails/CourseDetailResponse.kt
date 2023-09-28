package com.ias.gsscore.network.response.coursedetails

import com.google.gson.annotations.SerializedName
import com.ias.gsscore.network.response.cart.CartList

data class CourseDetailResponse(
    @SerializedName("status") var status : Boolean? = false,
    @SerializedName("cart_items") var cartItems : CartItems? = null,
    @SerializedName("message") var message : String? = null
    )

data class CartItems(
    @SerializedName("label_text") var labelText : String? = null,
    @SerializedName("label_value") var labelValue : String? = null,
    @SerializedName("total_items") var totalItems : Int? = 0,
    @SerializedName("total_amount") var totalAmount : Int? = 0,
    @SerializedName("total_coupon_amount") var totalCouponAmount : Int? = 0,
    @SerializedName("total_payable_amount") var totalPayableAmount : Double? = 0.0,
    @SerializedName("list") var courseList : ArrayList<CartList>? = null
)

data class Course(
    @SerializedName("id") var id : String? = null,
    @SerializedName("title") var title : String? = null,
    @SerializedName("courseName") var courseName : String? = null,
    @SerializedName("mode") var mode : String? = null,
    @SerializedName("amount") var amount : String? = null,
    @SerializedName("gst_amount") var gstAmount : Int? = 0,
    @SerializedName("coupon_amount") var couponAmount : Int? = 0,
    @SerializedName("payable_amount") var payableAmount : Int? = 0,
    @SerializedName("banner_img") var bannerImage : String? = "",
)