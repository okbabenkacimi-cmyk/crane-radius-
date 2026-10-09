package com.craneradius.models

import com.craneradius.data.AssessmentEntity
import com.craneradius.geometry.GeometryResult

data class CraneUiState(
    val cameraState: CameraUiState = CameraUiState.Idle,
    val boomLengthText: String = "",
    val boomAngleText: String = "",
    val pivotHeightText: String = "",
    val planningMarginM: Double = 0.0,
    val geometry: GeometryResult? = null,
    val errors: List<String> = emptyList(),
    val savedAssessments: List<AssessmentEntity> = emptyList()
)

sealed interface CameraUiState {
    data object Idle : CameraUiState
    data object PermissionRequired : CameraUiState
    data object PermissionDenied : CameraUiState
    data object Initializing : CameraUiState
    data object Ready : CameraUiState
    data class Error(val message: String) : CameraUiState
}
