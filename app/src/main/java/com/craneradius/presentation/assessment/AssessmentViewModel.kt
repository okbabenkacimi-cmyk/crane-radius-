package com.craneradius.presentation.assessment

import androidx.lifecycle.ViewModel
import com.craneradius.geometry.CraneGeometryEngine
import com.craneradius.geometry.GeometryInput
import com.craneradius.models.CameraUiState
import com.craneradius.models.CraneUiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class AssessmentViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        CraneUiState(cameraState = CameraUiState.PermissionRequired)
    )
    val uiState: StateFlow<CraneUiState> = _uiState.asStateFlow()

    fun onCameraPermissionGranted() {
        _uiState.update { it.copy(cameraState = CameraUiState.Initializing) }
    }

    fun onCameraPermissionDenied() {
        _uiState.update { it.copy(cameraState = CameraUiState.PermissionDenied) }
    }

    fun onCameraError(message: String) {
        _uiState.update { it.copy(cameraState = CameraUiState.Error(message)) }
    }

    fun retryCamera() {
        _uiState.update { it.copy(cameraState = CameraUiState.Initializing) }
    }

    fun onBoomLengthChanged(value: String) {
        _uiState.update { it.copy(boomLengthText = value) }
    }

    fun onBoomAngleChanged(value: String) {
        _uiState.update { it.copy(boomAngleText = value) }
    }

    fun onPivotHeightChanged(value: String) {
        _uiState.update { it.copy(pivotHeightText = value) }
    }

    fun adjustMargin(delta: Double) {
        _uiState.update {
            it.copy(planningMarginM = (it.planningMarginM + delta).coerceAtLeast(0.0))
        }
    }

    fun calculate() {
        val state = _uiState.value
        val errors = mutableListOf<String>()

        val length = state.boomLengthText.trim().toDoubleOrNull()
        val angle = state.boomAngleText.trim().toDoubleOrNull()
        val pivotHeight = state.pivotHeightText.trim().toDoubleOrNull()

        if (length == null) errors += "Boom length must be a valid number."
        if (angle == null) errors += "Boom angle must be a valid number."
        if (pivotHeight == null) errors += "Pivot height must be a valid number."

        if (errors.isNotEmpty()) {
            _uiState.update { it.copy(errors = errors) }
            return
        }

        val input = GeometryInput(
            boomLengthM = length!!,
            boomAngleDeg = angle!!,
            pivotHeightM = pivotHeight!!,
            planningMarginM = state.planningMarginM
        )

        CraneGeometryEngine.calculate(input)
            .onSuccess { result ->
                _uiState.update { it.copy(geometry = result, errors = emptyList()) }
            }
            .onFailure { exception ->
                _uiState.update {
                    it.copy(errors = listOf(exception.message ?: "Invalid input."))
                }
            }
    }
}
