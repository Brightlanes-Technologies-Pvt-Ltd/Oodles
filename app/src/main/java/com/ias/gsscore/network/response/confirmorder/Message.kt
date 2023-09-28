package com.ias.gsscore.network.response.confirmorder

import com.google.gson.annotations.SerializedName


data class Message (
  @SerializedName("user_id"             ) var user_id   : String? = null,
  @SerializedName("name"          ) var name            : String? = null,
  @SerializedName("email"           ) var email          : String? = null,
  @SerializedName("contact"         ) var contact        : String?    = null,
  @SerializedName("key"             ) var key             : String?    = null,
  @SerializedName("txnid"           ) var txnid           : String?    = null,
  @SerializedName("hash" ) var hash : String?    = null


)