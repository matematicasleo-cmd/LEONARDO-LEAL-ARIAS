package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import androidx.compose.runtime.staticCompositionLocalOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.PI
import kotlin.math.sin
import kotlin.random.Random

enum class SpaceSfx {
    SOFT_BEEP,
    WARP_JUMP,
    STAR_CHIRP,
    SHIELD_THUD
}

class SpaceSoundManager {

    private val scope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val _isMuted = MutableStateFlow(false)
    val isMuted: StateFlow<Boolean> = _isMuted.asStateFlow()

    private val preRenderedSamples: Map<SpaceSfx, ShortArray> by lazy {
        mapOf(
            SpaceSfx.SOFT_BEEP to synthesizeSoftBeep(),
            SpaceSfx.WARP_JUMP to synthesizeWarpJump(),
            SpaceSfx.STAR_CHIRP to synthesizeStarChirp(),
            SpaceSfx.SHIELD_THUD to synthesizeShieldThud()
        )
    }

    fun toggleMute() {
        val nextMuted = !_isMuted.value
        _isMuted.value = nextMuted
        if (!nextMuted) {
            play(SpaceSfx.SOFT_BEEP)
        }
    }

    fun play(sfx: SpaceSfx) {
        if (_isMuted.value) return
        scope.launch {
            runCatching {
                val samples = preRenderedSamples[sfx] ?: return@runCatching
                val bufferBytes = samples.size * 2
                val attributes = AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build()
                val format = AudioFormat.Builder()
                    .setSampleRate(SAMPLE_RATE)
                    .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                    .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                    .build()

                val track = AudioTrack.Builder()
                    .setAudioAttributes(attributes)
                    .setAudioFormat(format)
                    .setBufferSizeInBytes(bufferBytes)
                    .setTransferMode(AudioTrack.MODE_STATIC)
                    .build()

                track.write(samples, 0, samples.size)
                track.play()
                val durationMs = (samples.size * 1000L) / SAMPLE_RATE
                delay(durationMs + 50L)
                runCatching {
                    track.stop()
                    track.release()
                }
            }
        }
    }

    fun release() {
        scope.cancel()
    }

    private fun synthesizeSoftBeep(): ShortArray {
        val durationSec = 0.065
        val count = (SAMPLE_RATE * durationSec).toInt()
        return ShortArray(count) { i ->
            val t = i.toDouble() / SAMPLE_RATE
            val norm = i.toDouble() / count
            val env = sin(PI * norm)
            val freq = 820.0 + (220.0 * norm)
            val wave = sin(2.0 * PI * freq * t)
            (wave * env * 0.16 * Short.MAX_VALUE).toInt().toShort()
        }
    }

    private fun synthesizeWarpJump(): ShortArray {
        val durationSec = 0.24
        val count = (SAMPLE_RATE * durationSec).toInt()
        val rng = Random(42)
        var phaseMain = 0.0
        var phaseHarmonic = 0.0
        var smoothNoise = 0.0
        return ShortArray(count) { i ->
            val norm = i.toDouble() / count
            val env = sin(PI * norm) * (1.0 - norm * 0.25)
            // Soft ascending propulsion sweep + faint star-dust shimmer
            val freq = 175.0 + (390.0 * norm * norm)
            phaseMain += 2.0 * PI * freq / SAMPLE_RATE
            phaseHarmonic += 2.0 * PI * (freq * 2.0) / SAMPLE_RATE
            val rawNoise = (rng.nextDouble() * 2.0 - 1.0)
            smoothNoise = smoothNoise * 0.88 + rawNoise * 0.12
            val signal = sin(phaseMain) * 0.58 + sin(phaseHarmonic) * 0.22 + smoothNoise * 0.20
            (signal * env * 0.15 * Short.MAX_VALUE).toInt().toShort()
        }
    }

    private fun synthesizeStarChirp(): ShortArray {
        val durationSec = 0.16
        val count = (SAMPLE_RATE * durationSec).toInt()
        var phase = 0.0
        return ShortArray(count) { i ->
            val norm = i.toDouble() / count
            val env = sin(PI * norm)
            val freq = if (norm < 0.48) 987.77 else 1479.98
            phase += 2.0 * PI * freq / SAMPLE_RATE
            val shimmer = sin(phase * 1.5) * 0.25
            val wave = sin(phase) * 0.75 + shimmer
            (wave * env * 0.17 * Short.MAX_VALUE).toInt().toShort()
        }
    }

    private fun synthesizeShieldThud(): ShortArray {
        val durationSec = 0.11
        val count = (SAMPLE_RATE * durationSec).toInt()
        var phase = 0.0
        return ShortArray(count) { i ->
            val norm = i.toDouble() / count
            val env = (1.0 - norm) * sin(PI * norm)
            val freq = 210.0 - (85.0 * norm)
            phase += 2.0 * PI * freq / SAMPLE_RATE
            (sin(phase) * env * 0.18 * Short.MAX_VALUE).toInt().toShort()
        }
    }

    private companion object {
        const val SAMPLE_RATE = 22050
    }
}

val LocalSpaceSoundManager = staticCompositionLocalOf<SpaceSoundManager?> { null }
