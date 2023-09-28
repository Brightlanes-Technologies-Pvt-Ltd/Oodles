package com.ias.gsscore.network.response.testresult

import com.google.gson.annotations.SerializedName


data class Sections (

    @SerializedName("id"              ) var id              : String?           = null,
    @SerializedName("title"           ) var title           : String?           = null,
    @SerializedName("timeTaken"       ) var timeTaken       : Double?           = null,
    @SerializedName("totalQuestions"  ) var totalQuestions  : Int?              = null,
    @SerializedName("duration"        ) var duration        : Int?              = null,
    @SerializedName("attempt"         ) var attempt         : Attempt?          = Attempt(),
    @SerializedName("topics"          ) var topics          : ArrayList<Topics> = arrayListOf(),
    @SerializedName("gsscore"           ) var score           : Score?            = Score(),
    @SerializedName("recommendations" ) var recommendations : Recommendations?  = Recommendations()

)