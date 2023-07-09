package dev.nthduc.hoshino.utils

import kotlinx.datetime.Instant
import kotlinx.datetime.TimeZone
import kotlinx.datetime.toLocalDateTime

fun formatDate(instant: Instant): String {
    val localDateTime = instant.toLocalDateTime(TimeZone.currentSystemDefault())
    return "%02d/%02d/%04d".format(localDateTime.dayOfMonth, localDateTime.monthNumber, localDateTime.year)
}
