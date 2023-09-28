package com.ias.gsscore.network.response.address

import com.google.gson.annotations.SerializedName


data class AddressList (
  @SerializedName("id"              ) var id             : String? = null,
  @SerializedName("fullname"        ) var fullname       : String? = null,
  @SerializedName("mobile"          ) var mobile         : String? = null,
  @SerializedName("landmark"        ) var landmark       : String? = null,
  @SerializedName("state_name"      ) var stateName      : String? = null,
  @SerializedName("city"            ) var city           : String? = null,
  @SerializedName("address"         ) var address        : String? = null,
  @SerializedName("zip_code"        ) var zipCode        : String? = null,
  @SerializedName("default_address" ) var defaultAddress : String? = null
)