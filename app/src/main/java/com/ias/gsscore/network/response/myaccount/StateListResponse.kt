package com.ias.gsscore.network.response.myaccount

import com.google.gson.annotations.SerializedName
import com.ias.gsscore.network.response.OodlesApiResponse


data class StateListResponse (
  @SerializedName("state_list" ) var state_list : ArrayList<StateList> = arrayListOf()



):OodlesApiResponse()