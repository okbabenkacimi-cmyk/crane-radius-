package com.craneradius.data

import kotlinx.coroutines.flow.Flow

class AssessmentRepository(private val dao: AssessmentDao) {

    fun observeAll(): Flow<List<AssessmentEntity>> = dao.observeAll()

    suspend fun findById(id: Long): AssessmentEntity? = dao.findById(id)

    suspend fun save(assessment: AssessmentEntity): Long = dao.insert(assessment)

    suspend fun delete(assessment: AssessmentEntity) = dao.delete(assessment)
}
