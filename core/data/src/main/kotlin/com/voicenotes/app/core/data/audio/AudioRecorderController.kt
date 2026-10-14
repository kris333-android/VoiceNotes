package com.voicenotes.app.core.data.audio

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.os.SystemClock
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

/**
 * Wraps [MediaRecorder] to record voice notes to app-private storage, exposing recording
 * progress and live amplitude (for a waveform visualizer) as a [StateFlow].
 *
 * One instance is owned per screen/ViewModel that records audio; call [release] when done.
 */
class AudioRecorderController @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    sealed interface State {
        data object Idle : State
        data class Recording(val elapsedMillis: Long, val amplitude: Int) : State
        data class Paused(val elapsedMillis: Long) : State
    }

    data class RecordingResult(val file: File, val durationMillis: Long)

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state.asStateFlow()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private var pollingJob: Job? = null
    private var mediaRecorder: MediaRecorder? = null
    private var outputFile: File? = null
    private var accumulatedMillis: Long = 0L
    private var segmentStartUptime: Long? = null

    fun start(outputFile: File) {
        println("AudioRecorderController.start() outputFile=${outputFile.absolutePath}")
        releaseRecorder()
        val recorder = createRecorderAndConfigure(outputFile)
        mediaRecorder = recorder
        this.outputFile = outputFile
        accumulatedMillis = 0L
        segmentStartUptime = SystemClock.elapsedRealtime()
        startPolling()
    }

    fun pause() {
        val recorder = mediaRecorder ?: return
        if (_state.value !is State.Recording) return
        recorder.pause()
        pollingJob?.cancel()
        accumulatedMillis = currentElapsedMillis()
        segmentStartUptime = null
        _state.value = State.Paused(accumulatedMillis)
    }

    fun resume() {
        val recorder = mediaRecorder ?: return
        if (_state.value !is State.Paused) return
        recorder.resume()
        segmentStartUptime = SystemClock.elapsedRealtime()
        startPolling()
    }

    fun stop(): RecordingResult? {
        val recorder = mediaRecorder
        val file = outputFile
        val duration = currentElapsedMillis()
        pollingJob?.cancel()
        recorder?.let {
            runCatching { recorder.stop() }
            recorder.release()
        }
        cleanup()
        _state.value = State.Idle
        return file?.let { RecordingResult(it, duration) }
    }

    private fun cleanup() {
        mediaRecorder = null
        outputFile = null
    }

    fun cancel() {
        val file = outputFile
        stop()
        file?.let { runCatching { it.delete() } }
    }

    fun release() {
        pollingJob?.cancel()
        releaseRecorder()
        scope.cancel()
    }

    private fun currentElapsedMillis(): Long =
        accumulatedMillis + (segmentStartUptime?.let { SystemClock.elapsedRealtime() - it } ?: 0L)

    private fun startPolling() {
        pollingJob?.cancel()
        pollingJob = scope.launch {
            while (isActive) {
                val amplitude = runCatching { mediaRecorder?.maxAmplitude ?: 0 }.getOrDefault(0)
                _state.value = State.Recording(currentElapsedMillis(), amplitude)
                delay(POLL_INTERVAL_MS)
            }
        }
    }

    private fun releaseRecorder() {
        mediaRecorder?.let { runCatching { it.release() } }
        mediaRecorder = null
    }

    private fun createRecorderAndConfigure(output: File): MediaRecorder =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            MediaRecorder(context)
        } else {
            @Suppress("DEPRECATION")
            MediaRecorder()
        }.apply {
            setAudioSource(MediaRecorder.AudioSource.MIC)
            setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            setAudioEncodingBitRate(128_000)
            setAudioSamplingRate(44_100)
            setOutputFile(output.absolutePath)
            prepare()
            start()
        }

    private companion object {
        const val POLL_INTERVAL_MS = 100L
    }
}
