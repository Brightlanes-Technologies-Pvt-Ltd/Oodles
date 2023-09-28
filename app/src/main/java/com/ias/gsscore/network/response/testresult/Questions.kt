package com.ias.gsscore.network.response.testresult

import com.google.gson.annotations.SerializedName


data class Questions (

  @SerializedName("questionId"                        ) var questionId                        : String? = null,
  @SerializedName("questionInstruction"               ) var questionInstruction               : String? = null,
  @SerializedName("questionTitle"                     ) var questionTitle                     : String? = null,
  @SerializedName("optionOne"                         ) var optionOne                         : String = "",
  @SerializedName("optionTwo"                         ) var optionTwo                         : String = "",
  @SerializedName("optionThree"                       ) var optionThree                       : String = "",
  @SerializedName("optionFour"                        ) var optionFour                        : String = "",
  @SerializedName("optionFive"                        ) var optionFive                        : String = "",
  @SerializedName("correctAnswer"                     ) var correctAnswer                     : String? = null,
  @SerializedName("soultionTraditional"               ) var soultionTraditional               : String? = null,
  @SerializedName("smartSolution"                     ) var smartSolution                     : String? = null,
  @SerializedName("optionBasedApproach"               ) var optionBasedApproach               : String? = null,
  @SerializedName("videoType"                         ) var videoType                         : String? = null,
  @SerializedName("videoId"                           ) var videoId                           : String? = null,
  @SerializedName("section"                           ) var section                           : String? = null,
  @SerializedName("topic"                             ) var topic                             : String? = null,
  @SerializedName("subTopic"                          ) var subTopic                          : String? = null,
  @SerializedName("level"                             ) var level                             : String? = null,
  @SerializedName("round"                             ) var round                             : String? = null,
  @SerializedName("solvingTime"                       ) var solvingTime                       : String? = null,
  @SerializedName("isTrap"                            ) var isTrap                            : String? = null,
  @SerializedName("isSillyError"                      ) var isSillyError                      : String? = null,
  @SerializedName("isFluke"                           ) var isFluke                           : String? = null,
  @SerializedName("markedOption"                      ) var markedOption                      : String? = null,
  @SerializedName("userSolvingTime"                   ) var userSolvingTime                   : String? = null,
  @SerializedName("questionNumber"                    ) var questionNumber                    : String? = null,
  @SerializedName("bookmarkId"                        ) var bookmarkId                        : String? = null,
  @SerializedName("attemptType"                       ) var attemptType                       : String? = null,
  @SerializedName("marksForCorrectAnswer"             ) var marksForCorrectAnswer             : String? = null,
  @SerializedName("negativeMarkingForIncorrectAnswer" ) var negativeMarkingForIncorrectAnswer : Double? = null,
  @SerializedName("message"                           ) var message                           : String? = null,
  @SerializedName("totalAttempt"                      ) var totalAttempt                      : Int?    = null,
  @SerializedName("totalCorrectAttempt"               ) var totalCorrectAttempt               : Int?    = null

)