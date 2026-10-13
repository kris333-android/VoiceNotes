package com.voicenotes.app.core.data.audio

import android.media.MediaPlayer
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

/** Wraps [MediaPlayer] for note playback, exposing position/duration as a [StateFlow]. */
class AudioPlayerController @Inject constructor() {

    sealed interface State {
        data object Idle : State
        data class Playing(val positionMillis: Long, val durationMillis: Long) : State
        data class Paused(val positionMillis: Long, val durationMillis: Long) : State
        data class Completed(val durationMillis: Long) : State
    }

    private val _state = MutableStateFlow<State>(State.Idle)
    val state: StateFlow<State> = _state.asStateFlow()

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Main.immediate)
    private var pollingJob: Job? = null
    private var mediaPlayer: MediaPlayer? = null

    fun load(file: File) {
        releasePlayer()
        val mp = MediaPlayer().apply {
            setDataSource(file.absolutePath)
            setOnCompletionListener {
                pollingJob?.cancel()
                _state.value = State.Completed(duration.toLong())
            }
            prepare()
        }
        mediaPlayer = mp
        _state.value = State.Paused(0L, mp.duration.toLong())
    }

    fun play() {
        val mp = mediaPlayer ?: return
        if (_state.value is State.Completed) {
            mp.seekTo(0)
        }
        mp.start()
        startPolling()
    }

    fun pause() {
        val mp = mediaPlayer ?: return
        if (!mp.isPlaying) return
        mp.pause()
        pollingJob?.cancel()
        _state.value = State.Paused(mp.currentPosition.toLong(), mp.duration.toLong())
    }

    fun seekTo(positionMillis: Long) {
        mediaPlayer?.seekTo(positionMillis.toInt())
    }

    fun release() {
        pollingJob?.cancel()
        releasePlayer()
        scope.cancel()
    }

    private fun startPolling() {
        pollingJob?.cancel()
        pollingJob = scope.launch {
            while (isActive) {
                val mp = mediaPlayer ?: break
                _state.value = State.Playing(mp.currentPosition.toLong(), mp.duration.toLong())
                delay(POLL_INTERVAL_MS)
            }
        }
    }

    private fun releasePlayer() {
        pollingJob?.cancel()
        mediaPlayer?.let { runCatching { it.release() } }
        mediaPlayer = null
        _state.value = State.Idle
    }

    private companion object {
        const val POLL_INTERVAL_MS = 150L
    }
}
