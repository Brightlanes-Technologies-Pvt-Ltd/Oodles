package com.example.example

import com.google.gson.annotations.SerializedName


data class WeakArea (

  @SerializedName("total"    ) var total    : Int?              = null,
  @SerializedName("sections" ) var sections : ArrayList<String> = arrayListOf()

)