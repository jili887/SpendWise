package com.research.android.spendwise.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.util.Locale

private val DATE_FORMATTER =
    DateTimeFormatter.ofPattern(
        "MM/dd/yyyy",
        Locale.US
    )

fun millisToDateString(millis: Long): String {
    return Instant
        .ofEpochMilli(millis)
        .atZone(ZoneOffset.UTC)
        .toLocalDate()
        .format(DATE_FORMATTER)
}

fun dateStringToMillis(dateString: String): Long? {
    if (dateString.isBlank()) {
        return null
    }

    return try {
        LocalDate
            .parse(dateString, DATE_FORMATTER)
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
    } catch (e: Exception) {
        null
    }
}
