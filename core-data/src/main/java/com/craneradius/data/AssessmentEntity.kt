package com.craneradius.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "assessments")
data class AssessmentEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,
    val name: String,
    val boomLengthM: Double,
    val boomAngleDeg: Double,
    val pivotHeightM: Double,
    val planningMarginM: Double,
    val sourceLabel: String = "Manual entry",
    val statusLabel: String = "User-verified",
    val createdAtEpochMs: Long = System.currentTimeMillis()
)
