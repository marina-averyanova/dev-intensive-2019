package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))

    return dateFormat.format(this)
}

fun Date.add(value: Long, units: TimeUnits): Date {
    var time = this.time

    time += when (units) {
        TimeUnits.SECOND -> TimeUnit.SECONDS.toMillis(value)
        TimeUnits.MINUTE -> TimeUnit.MINUTES.toMillis(value)
        TimeUnits.HOUR -> TimeUnit.HOURS.toMillis(value)
        TimeUnits.DAY -> TimeUnit.DAYS.toMillis(value)
    }
    this.time = time

    return this
}

// TODO
fun Date.humanizeDiff() {}

enum class TimeUnits {
    SECOND,
    MINUTE,
    HOUR,
    DAY
}