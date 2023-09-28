package com.ias.gsscore.network.response.myaccount

import com.google.gson.annotations.SerializedName


data class ProgramList (

  @SerializedName("program_id"          ) var programId          : String? = null,
  @SerializedName("title"       ) var programTitle       : String? = null,
  var bool:Boolean = false
)