package com.voicenotes.app.core.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.semantics.clearAndSetSemantics
import androidx.compose.ui.unit.dp

/**
 * Draws a live bar waveform from recent amplitude samples (most recent last, values 0f..1f).
 * Purely decorative — the recording state is announced separately via [Modifier.clearAndSetSemantics],
 * so screen readers don't try to read every redrawn frame.
 */
@Composable
fun WaveformVisualizer(
    amplitudes: List<Float>,
    modifier: Modifier = Modifier,
    barColor: Color = MaterialTheme.colorScheme.primary,
) {
    Canvas(
        modifier = modifier
            .fillMaxWidth()
            .height(96.dp)
            .clearAndSetSemantics { },
    ) {
        if (amplitudes.isEmpty()) return@Canvas
        drawBars(amplitudes, barColor)
    }
}

private fun DrawScope.drawBars(amplitudes: List<Float>, color: Color) {
    val barSpacing = 4.dp.toPx()
    val maxBars = (size.width / barSpacing).toInt().coerceAtLeast(1)
    val visible = amplitudes.takeLast(maxBars)
    val barWidth = (size.width / maxBars) - barSpacing
    val centerY = size.height / 2f

    visible.forEachIndexed { index, amplitude ->
        val barHeight = (size.height * amplitude.coerceIn(0.04f, 1f))
        val x = index * (barWidth + barSpacing)
        drawRoundRect(
            color = color,
            topLeft = androidx.compose.ui.geometry.Offset(x, centerY - barHeight / 2f),
            size = androidx.compose.ui.geometry.Size(barWidth, barHeight),
            cornerRadius = CornerRadius(barWidth / 2f, barWidth / 2f),
        )
    }
}
