package com.ias.gsscore.network.response.testresult

import com.google.gson.annotations.SerializedName


data class AverageArea (

  @SerializedName("total"    ) var total    : Int?              = null,
  @SerializedName("sections" ) var sections : ArrayList<String> = arrayListOf()

)