package com.craneradius.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface AssessmentDao {

    @Query("SELECT * FROM assessments ORDER BY createdAtEpochMs DESC")
    fun observeAll(): Flow<List<AssessmentEntity>>

    @Query("SELECT * FROM assessments WHERE id = :id")
    suspend fun findById(id: Long): AssessmentEntity?

    @Insert
    suspend fun insert(assessment: AssessmentEntity): Long

    @Delete
    suspend fun delete(assessment: AssessmentEntity)
}
