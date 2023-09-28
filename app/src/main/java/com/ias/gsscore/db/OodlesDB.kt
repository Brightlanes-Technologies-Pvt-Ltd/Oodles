package com.ias.gsscore.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ias.gsscore.db.model.TestQuestionModel

@Database(entities = [TestQuestionModel::class], version = 1)
abstract class OodlesDB : RoomDatabase() {
    abstract fun oodlesDao(): OodlesDao
}