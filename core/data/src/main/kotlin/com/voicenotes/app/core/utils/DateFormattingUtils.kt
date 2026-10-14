package com.voicenotes.app.core.utils

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class DateFormattingUtils @Inject constructor() {

    fun getCurrentTimestampFormatted(): String =
        DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss").format(LocalDateTime.now())

    fun getFileNameForUi(): String =
        DateTimeFormatter.ofPattern("MMM d, h:mm a").format(LocalDateTime.now())
}