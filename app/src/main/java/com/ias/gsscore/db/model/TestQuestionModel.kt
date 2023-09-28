package com.ias.gsscore.db.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class TestQuestionModel(
    @PrimaryKey(autoGenerate = true)
    var id: Int,
    var testId: String,
    var questionId: String,
    var attemptOrder: Int,
    var attemptType: String,
    var selectedOption: String,
    var solvingTime:Int
)