package com.voicenotes.app.core.common

import org.junit.Assert.assertEquals
import org.junit.Test

class DurationFormatTest {

    @Test
    fun `formats seconds under a minute as m colon ss`() {
        assertEquals("0:09", formatDurationMillis(9_000))
    }

    @Test
    fun `formats minutes and seconds`() {
        assertEquals("2:05", formatDurationMillis(125_000))
    }

    @Test
    fun `formats hours when present`() {
        assertEquals("1:00:00", formatDurationMillis(3_600_000))
    }

    @Test
    fun `accessibility label pluralizes correctly`() {
        assertEquals("1 minute 1 second", formatDurationForAccessibility(61_000))
        assertEquals("2 minutes 5 seconds", formatDurationForAccessibility(125_000))
    }

    @Test
    fun `accessibility label handles zero duration`() {
        assertEquals("0 seconds", formatDurationForAccessibility(0))
    }
}
