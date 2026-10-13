package com.voicenotes.app.core.data.share

import android.content.Context
import android.content.Intent
import androidx.core.content.FileProvider
import com.voicenotes.app.core.data.R
import com.voicenotes.app.core.model.VoiceNote
import dagger.hilt.android.qualifiers.ApplicationContext
import java.io.File
import javax.inject.Inject

/** Builds a chooser [Intent] so a voice note can be shared to email, WhatsApp, or any app supporting audio. */
class ShareNoteHelper @Inject constructor(
    @ApplicationContext private val context: Context,
) {
    fun shareIntentFor(note: VoiceNote): Intent {
        val file = File(note.filePath)
        val uri = FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
        val sendIntent = Intent(Intent.ACTION_SEND).apply {
            type = "audio/mp4"
            putExtra(Intent.EXTRA_STREAM, uri)
            putExtra(Intent.EXTRA_SUBJECT, note.title)
            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
        }
        return Intent.createChooser(sendIntent, context.getString(R.string.share_chooser_title))
    }

    /** Launches the share chooser directly; safe to call from a ViewModel since this uses the app context. */
    fun share(note: VoiceNote) {
        context.startActivity(shareIntentFor(note).addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }
}
