package com.voicenotes.app.feature.record

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.provider.Settings
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityCompat
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.LiveRegionMode
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.liveRegion
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.voicenotes.app.core.common.formatDurationForAccessibility
import com.voicenotes.app.core.common.formatDurationMillis
import com.voicenotes.app.core.designsystem.theme.VoiceNotesTheme
import com.voicenotes.app.core.ui.ConfirmActionDialog
import com.voicenotes.app.core.ui.WaveformVisualizer

@Composable
fun RecordRoute(
    onDone: () -> Unit,
    viewModel: RecordViewModel = hiltViewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(uiState.savedNoteId) {
        if (uiState.savedNoteId != null) onDone()
    }

    RecordScreen(
        uiState = uiState,
        onStart = viewModel::startRecording,
        onPause = viewModel::pauseRecording,
        onResume = viewModel::resumeRecording,
        onFinish = viewModel::finishAndSave,
        onDiscard = viewModel::discardRecording,
        onClose = onDone,
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecordScreen(
    uiState: RecordUiState,
    onStart: () -> Unit,
    onPause: () -> Unit,
    onResume: () -> Unit,
    onFinish: () -> Unit,
    onDiscard: () -> Unit,
    onClose: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val context = LocalContext.current
    var hasMicPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
                PackageManager.PERMISSION_GRANTED,
        )
    }
    var deniedPermissionAttempts by rememberSaveable { mutableStateOf(0) }
    var showAppSettings by rememberSaveable { mutableStateOf(false) }
    var showDiscardConfirm by rememberSaveable { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
    ) { granted ->
        hasMicPermission = granted
        if (granted) {
            deniedPermissionAttempts = 0
            showAppSettings = false
            onStart()
        } else {
            deniedPermissionAttempts++
            val shouldShowRationale = context.findActivity()?.let {
                ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.RECORD_AUDIO)
            } ?: true
            if (deniedPermissionAttempts >= 2 || !shouldShowRationale) showAppSettings = true
        }
    }
    val lifecycleOwner = LocalLifecycleOwner.current

    LaunchedEffect(Unit) {
        if (!hasMicPermission) permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
    }

    DisposableEffect(lifecycleOwner, context) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                hasMicPermission =
                    ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) ==
                    PackageManager.PERMISSION_GRANTED
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("New voice note") },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (uiState.phase == RecordUiState.Phase.IDLE) onClose() else showDiscardConfirm = true
                        },
                    ) {
                        Icon(Icons.Filled.Close, contentDescription = "Close, discard recording")
                    }
                },
            )
        },
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            val elapsedLabel = formatDurationMillis(uiState.elapsedMillis)
            val elapsedA11y = formatDurationForAccessibility(uiState.elapsedMillis)

            Text(
                text = elapsedLabel,
                style = MaterialTheme.typography.titleLarge,
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .semantics {
                        liveRegion = LiveRegionMode.Polite
                        contentDescription = "Recording time: $elapsedA11y"
                    },
            )

            WaveformVisualizer(amplitudes = uiState.amplitudeHistory)

            Spacer(modifier = Modifier.height(48.dp))

            when (uiState.phase) {
                RecordUiState.Phase.IDLE -> {
                    FloatingActionButton(
                        onClick = {
                            if (hasMicPermission) {
                                onStart()
                            } else {
                                permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        },
                        modifier = Modifier
                            .size(80.dp)
                            .semantics { contentDescription = "Start recording" },
                    ) {
                        Icon(Icons.Filled.Mic, contentDescription = null, modifier = Modifier.size(36.dp))
                    }
                    if (!hasMicPermission) {
                        Text(
                            text = if (showAppSettings) {
                                "Microphone access is disabled. Enable it in app settings to record voice notes."
                            } else {
                                "Microphone access is needed to record voice notes. Tap the microphone to try again."
                            },
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(top = 16.dp),
                        )
                        if (showAppSettings) {
                            Button(
                                onClick = {
                                    context.startActivity(
                                        Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                            data = Uri.fromParts("package", context.packageName, null)
                                        },
                                    )
                                },
                                modifier = Modifier.padding(top = 8.dp),
                            ) {
                                Text("Open app settings")
                            }
                        }
                    }
                }
                RecordUiState.Phase.RECORDING, RecordUiState.Phase.PAUSED -> {
                    if (uiState.isSaving) {
                        CircularProgressIndicator()
                    } else {
                        Row(
                            horizontalArrangement = Arrangement.spacedBy(32.dp),
                            verticalAlignment = Alignment.CenterVertically,
                        ) {
                            IconButton(
                                onClick = { showDiscardConfirm = true },
                                modifier = Modifier.size(56.dp),
                            ) {
                                Icon(
                                    Icons.Filled.Delete,
                                    contentDescription = "Discard recording",
                                    tint = MaterialTheme.colorScheme.error,
                                )
                            }

                            val isPaused = uiState.phase == RecordUiState.Phase.PAUSED
                            FloatingActionButton(
                                onClick = { if (isPaused) onResume() else onPause() },
                                modifier = Modifier
                                    .size(72.dp)
                                    .semantics { contentDescription = if (isPaused) "Resume recording" else "Pause recording" },
                            ) {
                                Icon(
                                    imageVector = if (isPaused) Icons.Filled.PlayArrow else Icons.Filled.Pause,
                                    contentDescription = null,
                                )
                            }

                            IconButton(
                                onClick = onFinish,
                                modifier = Modifier.size(56.dp),
                            ) {
                                Icon(
                                    Icons.Filled.Stop,
                                    contentDescription = "Stop and save recording",
                                    tint = MaterialTheme.colorScheme.primary,
                                )
                            }
                        }
                    }
                }
            }
        }
    }

    if (showDiscardConfirm) {
        ConfirmActionDialog(
            title = "Discard recording?",
            message = "This voice note has not been saved. Are you sure you want to discard it?",
            confirmLabel = "Discard",
            onDismiss = { showDiscardConfirm = false },
            onConfirm = {
                showDiscardConfirm = false
                onDiscard()
                onClose()
            },
        )
    }
}

private fun Context.findActivity(): Activity? {
    var currentContext = this
    while (currentContext is ContextWrapper) {
        if (currentContext is Activity) return currentContext
        currentContext = currentContext.baseContext
    }
    return null
}

@Preview(showBackground = true)
@Preview(showBackground = true, uiMode = android.content.res.Configuration.UI_MODE_NIGHT_YES)
@Composable
private fun RecordScreenPreview() {
    VoiceNotesTheme {
        RecordScreen(
            uiState = RecordUiState(
                phase = RecordUiState.Phase.RECORDING,
                elapsedMillis = 42_000L,
                amplitudeHistory = List(40) { (it % 10) / 10f },
            ),
            onStart = {},
            onPause = {},
            onResume = {},
            onFinish = {},
            onDiscard = {},
            onClose = {},
        )
    }
}
