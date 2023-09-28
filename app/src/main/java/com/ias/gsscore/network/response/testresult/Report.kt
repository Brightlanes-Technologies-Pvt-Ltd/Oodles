package com.ias.gsscore.network.response.testresult

import com.google.gson.annotations.SerializedName
import com.ias.gsscore.network.response.myaccount.TopperResult


data class Report (

    @SerializedName("correctQuestions"                  ) var correctQuestions                  : String?              = null,
    @SerializedName("incorrectQuestions"                ) var incorrectQuestions                : String?              = null,
    @SerializedName("unattemptedQuestions"              ) var unattemptedQuestions              : String?              = null,
    @SerializedName("attemptedQuestions"                ) var attemptedQuestions                : Int?                 = null,
    @SerializedName("totalQuestions"                    ) var totalQuestions                    : Int?                 = null,
    @SerializedName("testTitle"                         ) var testTitle                         : String?              = null,
    @SerializedName("testSlug"                          ) var testSlug                          : String?              = null,
    @SerializedName("testId"                            ) var testId                            : String?              = null,
    @SerializedName("testType"                          ) var testType                          : String?              = null,
    @SerializedName("testMode"                          ) var testMode                          : String?              = null,
    @SerializedName("questionPdf"                       ) var questionPdf                       : String?              = null,
    @SerializedName("solutionPdf"                       ) var solutionPdf                       : String?              = null,
    @SerializedName("examId"                            ) var examId                            : String?              = null,
    @SerializedName("marksForCorrectAnswer"             ) var marksForCorrectAnswer             : String?              = null,
    @SerializedName("negativeMarkingForIncorrectAnswer" ) var negativeMarkingForIncorrectAnswer : String?              = null,
    @SerializedName("bestScore"                         ) var bestScore                         : String?              = null,
    @SerializedName("avgScore"                          ) var avgScore                          : String?              = null,
    @SerializedName("myScore"                           ) var myScore                           : String?              = null,
    @SerializedName("maxMarks"                          ) var maxMarks                          : String?              = null,
    @SerializedName("rank"                              ) var rank                              : String?              = null,
    @SerializedName("totalStudent"                      ) var totalStudent                      : String?              = null,
    @SerializedName("accuracy"                          ) var accuracy                          : String?              = null,
    @SerializedName("percentile"                        ) var percentile                        : String?                 = null,
    @SerializedName("productiveTime"                    ) var productiveTime                    : String?              = null,
    @SerializedName("productiveTimeSeconds"             ) var productiveTimeSeconds             : String?              = null,
    @SerializedName("unProductiveTime"                  ) var unProductiveTime                  : String?              = null,
    @SerializedName("unProductiveTimeSeconds"           ) var unProductiveTimeSeconds           : String?              = null,
    @SerializedName("sections"                          ) var sections                          : ArrayList<Sections>  = arrayListOf(),
    @SerializedName("attemptLog"                        ) var attemptLog                        : AttemptLog?          = AttemptLog(),
    @SerializedName("questions"                         ) var questions                         : ArrayList<Questions> = arrayListOf(),
    @SerializedName("negativeScore"                     ) var negativeScore                     : String?              = null,
    @SerializedName("trueScore"                         ) var trueScore                         : Int?                 = null,
    @SerializedName("recommendations"                   ) var recommendations                   : Recommendations?     = Recommendations(),
    @SerializedName("topperResponse"                    ) var topperResponse                    : Map<String, TopperResult>?      = HashMap(),
    @SerializedName("modelMessage"                      ) var modelMessage                      : String?              = null,
    @SerializedName("showReport"                        ) var showReport                        : Boolean?             = null

)