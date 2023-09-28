package com.example.example

import com.google.gson.annotations.SerializedName


data class PackageProgramDetails (

  @SerializedName("package_id"          ) var packageId         : String? = null,
  @SerializedName("package_title"       ) var packageTitle      : String? = null,
  @SerializedName("package_code"        ) var packageCode       : String? = null,
  @SerializedName("package_batch_start" ) var packageBatchStart : String? = null,
  @SerializedName("program_id"          ) var programId         : String? = null,
  @SerializedName("program_title"       ) var programTitle      : String? = null

)