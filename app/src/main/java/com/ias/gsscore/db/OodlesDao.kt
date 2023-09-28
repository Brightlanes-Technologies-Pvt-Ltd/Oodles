package com.ias.gsscore.db

import androidx.room.*
import com.ias.gsscore.db.model.TestQuestionModel


@Dao
interface OodlesDao {
    @Insert
    fun insert(data: TestQuestionModel)

    @Query("SELECT * FROM TestQuestionModel WHERE testId = :id")
    fun  getAll(id: String?): MutableList<TestQuestionModel>

    @Update
    fun update(data: TestQuestionModel)

  /*  @Query("SELECT EXISTS(SELECT * FROM questionModel WHERE questionId = :questionId)")
    fun isQuestionExist(questionId: String): Boolean?*/

    @Query("SELECT * FROM TestQuestionModel WHERE questionId = :id")
    fun isQuestionExist(id: String?): TestQuestionModel

    @Query("delete from TestQuestionModel where testId = :idList")
    fun deleteTestId(idList: String)

    @Query("delete from TestQuestionModel where testId in (:idList)")
    fun deleteMultipleTest(idList: List<String>)

}