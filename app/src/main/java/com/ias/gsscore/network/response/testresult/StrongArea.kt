package com.ias.gsscore.network.response.testresult

import com.google.gson.annotations.SerializedName


data class StrongArea (

  @SerializedName("total"    ) var total    : Int?              = null,
  @SerializedName("sections" ) var sections : ArrayList<String> = arrayListOf()

)