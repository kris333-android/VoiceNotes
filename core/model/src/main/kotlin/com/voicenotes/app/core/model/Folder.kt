package com.voicenotes.app.core.model

import java.time.Instant

/**
 * A user-created folder used to organize voice notes.
 * Notes with a null `folderId` are considered "Unfiled" and are not tied to a [Folder].
 */
data class Folder(
    val id: Long = 0L,
    val name: String,
    val createdAt: Instant,
)

/** A folder paired with how many notes currently live in it, for list display. */
data class FolderWithNoteCount(
    val folder: Folder,
    val noteCount: Int,
)

/** Sentinel id used in navigation/UI to represent the virtual "All notes" collection. */
const val ALL_NOTES_FOLDER_ID: Long = -1L

/** Sentinel id used in navigation/UI to represent notes that have not been filed into a folder. */
const val UNFILED_FOLDER_ID: Long = -2L
