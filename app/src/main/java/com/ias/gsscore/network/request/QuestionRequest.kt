package com.ias.gsscore.network.request

class QuestionRequest {
    var attemptOrder: Int = 0
    var attemptType: String = "notVisited"
    var questionId: String = ""
    var selectedOption: String = ""
    var solvingTime: Int = 0
    //notAnswered
    //notVisited
    //answered
    //markForReview
}


