package com.ias.gsscore.network.response.myaccount

import com.google.gson.annotations.SerializedName


data class TopperData (

  @SerializedName("topperId"             ) var userId             : String? = null,
  @SerializedName("topperName"           ) var userName           : String? = null,
  @SerializedName("testScore"          ) var testScore          : String? = null,
)