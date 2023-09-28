package com.ias.gsscore.network.request

class TestSubmitRequest {
    var userId: String = ""
    var testId: String = ""
    var testType: String = ""
    var testResultId: String = ""
    var timeTaken: String = ""
   // var selectedQuestions: String = ""
    var selectedQuestions: HashMap<Int, QuestionRequest> = HashMap()
}


