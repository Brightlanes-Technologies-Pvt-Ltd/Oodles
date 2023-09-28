package com.ias.gsscore.network.response.courses

import com.google.gson.annotations.SerializedName


data class Filter (
    var title: String = "",
    var categoryList   : ArrayList<Category>   = arrayListOf(),
    @SerializedName("category"    ) var category   : ArrayList<Category>   = arrayListOf(),
    @SerializedName("mode"        ) var mode       : ArrayList<Category>       = arrayListOf(),
    @SerializedName("start_on"    ) var startOn    : ArrayList<Category>    = arrayListOf(),
    @SerializedName("course_type" ) var courseType : ArrayList<Category> = arrayListOf()

)