package com.ias.gsscore.network.response.testresult

import com.example.example.WeakArea
import com.google.gson.annotations.SerializedName


data class Recommendations (

    @SerializedName("strongArea"  ) var strongArea  : StrongArea?  = StrongArea(),
    @SerializedName("averageArea" ) var averageArea : AverageArea? = AverageArea(),
    @SerializedName("weakArea"    ) var weakArea    : WeakArea?    = WeakArea()

)