package com.ias.gsscore.network.response.myaccount

import com.google.gson.annotations.SerializedName


data class StateList (

  @SerializedName("id"               ) var id              : String? = null,
  @SerializedName("state_name"            ) var state_name           : String? = null


)