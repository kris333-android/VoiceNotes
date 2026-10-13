package com.voicenotes.app.feature.record

import android.Manifest
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.rule.GrantPermissionRule
import com.voicenotes.app.core.designsystem.theme.VoiceNotesTheme
import org.junit.Rule
import org.junit.Test
import org.junit.rules.RuleChain

class RecordScreenTest {

    private val composeTestRule = createComposeRule()

    @get:Rule
    val ruleChain: RuleChain = RuleChain
        .outerRule(GrantPermissionRule.grant(Manifest.permission.RECORD_AUDIO))
        .around(composeTestRule)

    private fun setContent(
        uiState: RecordUiState = RecordUiState(),
        onStart: () -> Unit = {},
        onPause: () -> Unit = {},
        onResume: () -> Unit = {},
        onFinish: () -> Unit = {},
        onDiscard: () -> Unit = {},
        onClose: () -> Unit = {},
    ) {
        composeTestRule.setContent {
            VoiceNotesTheme {
                RecordScreen(
                    uiState = uiState,
                    onStart = onStart,
                    onPause = onPause,
                    onResume = onResume,
                    onFinish = onFinish,
                    onDiscard = onDiscard,
                    onClose = onClose,
                )
            }
        }
    }

    @Test
    fun idlePhaseTappingMicInvokesOnStartWhenPermissionGranted() {
        var started = false
        setContent(uiState = RecordUiState(phase = RecordUiState.Phase.IDLE), onStart = { started = true })

        composeTestRule.onNodeWithContentDescription("Start recording").performClick()

        assert(started)
    }

    @Test
    fun recordingPhaseShowsElapsedTimeAndControls() {
        setContent(uiState = RecordUiState(phase = RecordUiState.Phase.RECORDING, elapsedMillis = 65_000L))

        composeTestRule.onNodeWithText("1:05").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Pause recording").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Discard recording").assertIsDisplayed()
        composeTestRule.onNodeWithContentDescription("Stop and save recording").assertIsDisplayed()
    }

    @Test
    fun tappingPauseInvokesOnPause() {
        var paused = false
        setContent(uiState = RecordUiState(phase = RecordUiState.Phase.RECORDING), onPause = { paused = true })

        composeTestRule.onNodeWithContentDescription("Pause recording").performClick()

        assert(paused)
    }

    @Test
    fun tappingStopInvokesOnFinish() {
        var finished = false
        setContent(uiState = RecordUiState(phase = RecordUiState.Phase.RECORDING), onFinish = { finished = true })

        composeTestRule.onNodeWithContentDescription("Stop and save recording").performClick()

        assert(finished)
    }

    @Test
    fun pausedPhaseShowsResumeAndInvokesOnResume() {
        var resumed = false
        setContent(uiState = RecordUiState(phase = RecordUiState.Phase.PAUSED), onResume = { resumed = true })

        composeTestRule.onNodeWithContentDescription("Resume recording").performClick()

        assert(resumed)
    }

    @Test
    fun tappingDiscardShowsConfirmationThenInvokesOnDiscardAndOnClose() {
        var discarded = false
        var closed = false
        setContent(
            uiState = RecordUiState(phase = RecordUiState.Phase.RECORDING),
            onDiscard = { discarded = true },
            onClose = { closed = true },
        )

        composeTestRule.onNodeWithContentDescription("Discard recording").performClick()
        composeTestRule.onNodeWithText("Discard recording?").assertIsDisplayed()
        composeTestRule.onNodeWithText("Discard").performClick()

        assert(discarded)
        assert(closed)
    }
}
