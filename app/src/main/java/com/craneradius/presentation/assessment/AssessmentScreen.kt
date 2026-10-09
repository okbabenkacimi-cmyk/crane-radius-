package com.craneradius.presentation.assessment

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.craneradius.camera.CameraErrorState
import com.craneradius.camera.CameraPreview
import com.craneradius.models.CameraUiState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AssessmentScreen(
    onBack: () -> Unit,
    viewModel: AssessmentViewModel = viewModel()
) {
    val state by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted) viewModel.onCameraPermissionGranted()
        else viewModel.onCameraPermissionDenied()
    }

    var hasRequestedPermission by remember { mutableStateOf(false) }

    if (!hasRequestedPermission) {
        hasRequestedPermission = true
        val alreadyGranted = ContextCompat.checkSelfPermission(
            context, Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED

        if (alreadyGranted) viewModel.onCameraPermissionGranted()
        else permissionLauncher.launch(Manifest.permission.CAMERA)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Crane Assessment") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(360.dp)
                    .background(Color.Black)
            ) {
                when (val cam = state.cameraState) {
                    is CameraUiState.PermissionDenied -> Text(
                        "Camera permission denied. You can still enter measurements manually.",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center).padding(24.dp)
                    )
                    is CameraUiState.Initializing -> Text(
                        "Starting camera…",
                        color = Color.White,
                        modifier = Modifier.align(Alignment.Center)
                    )
                    is CameraUiState.Ready -> CameraPreview(
                        modifier = Modifier.fillMaxSize(),
                        onReady = { },
                        onError = { viewModel.onCameraError(it) }
                    )
                    is CameraUiState.Error -> CameraErrorState(
                        message = cam.message,
                        onRetry = { viewModel.retryCamera() }
                    )
                    else -> { }
                }
            }

            Spacer(Modifier.height(16.dp))

            Card(modifier = Modifier.fillMaxWidth()) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        "Boom geometry (manual entry)",
                        style = MaterialTheme.typography.titleMedium
                    )
                    Spacer(Modifier.height(12.dp))

                    OutlinedTextField(
                        value = state.boomLengthText,
                        onValueChange = viewModel::onBoomLengthChanged,
                        label = { Text("Boom length L (m)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.boomAngleText,
                        onValueChange = viewModel::onBoomAngleChanged,
                        label = { Text("Boom angle (deg)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(
                        value = state.pivotHeightText,
                        onValueChange = viewModel::onPivotHeightChanged,
                        label = { Text("Pivot height H (m)") },
                        singleLine = true,
                        modifier = Modifier.fillMaxWidth()
                    )
                    Spacer(Modifier.height(8.dp))
                    Text("Planning margin M: %.2f m".format(state.planningMarginM))
                    Row {
                        Button(onClick = { viewModel.adjustMargin(-0.5) }) { Text("-0.5") }
                        Spacer(Modifier.width(8.dp))
                        Button(onClick = { viewModel.adjustMargin(0.5) }) { Text("+0.5") }
                    }
                    Spacer(Modifier.height(12.dp))
                    Button(
                        onClick = viewModel::calculate,
                        modifier = Modifier.fillMaxWidth()
                    ) { Text("Calculate") }
                }
            }

            state.geometry?.let { geo ->
                Spacer(Modifier.height(16.dp))
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Results", style = MaterialTheme.typography.titleMedium)
                        Spacer(Modifier.height(8.dp))
                        ResultRow("Horizontal radius R", "%.3f m".format(geo.horizontalRadiusM))
                        ResultRow("Vertical component V", "%.3f m".format(geo.verticalComponentM))
                        ResultRow("Tip height Z_tip", "%.3f m".format(geo.tipHeightM))
                        ResultRow(
                            "Planning boundary",
                            "%.3f m".format(geo.boundaryRadiusM),
                            MaterialTheme.colorScheme.error
                        )
                    }
                }
            }

            if (state.errors.isNotEmpty()) {
                Spacer(Modifier.height(12.dp))
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            "Validation",
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.titleSmall
                        )
                        state.errors.forEach { error ->
                            Text(
                                "• $error",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(32.dp))
        }
    }
}

@Composable
private fun ResultRow(label: String, value: String, valueColor: Color = MaterialTheme.colorScheme.onSurface) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(vertical = 2.dp)
    ) {
        Text(label, style = MaterialTheme.typography.bodyMedium, modifier = Modifier.weight(1f))
        Text(value, style = MaterialTheme.typography.bodyMedium, color = valueColor)
    }
}
