package com.craneradius.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [AssessmentEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AssessmentDatabase : RoomDatabase() {

    abstract fun assessmentDao(): AssessmentDao

    companion object {
        @Volatile
        private var INSTANCE: AssessmentDatabase? = null

        fun getInstance(context: Context): AssessmentDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: Room.databaseBuilder(
                    context.applicationContext,
                    AssessmentDatabase::class.java,
                    "crane_radius.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { INSTANCE = it }
            }
        }
    }
}
