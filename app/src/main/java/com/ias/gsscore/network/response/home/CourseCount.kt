package com.ias.gsscore.network.response.home

import com.google.gson.annotations.SerializedName


data class CourseCount (

  @SerializedName("gs_classes"       ) var gsClasses       : Int? = null,
  @SerializedName("optional_classes" ) var optionalClasses : Int? = null,
  @SerializedName("test_series"      ) var testSeries      : Int? = null

)