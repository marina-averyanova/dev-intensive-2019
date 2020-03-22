package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit
import kotlin.math.abs

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))

    return dateFormat.format(this)
}

fun Date.add(value: Long, units: TimeUnits): Date {
    var time = this.time

    time += when (units) {
        TimeUnits.MILLISECOND -> value
        TimeUnits.SECOND -> TimeUnit.SECONDS.toMillis(value)
        TimeUnits.MINUTE -> TimeUnit.MINUTES.toMillis(value)
        TimeUnits.HOUR -> TimeUnit.HOURS.toMillis(value)
        TimeUnits.DAY -> TimeUnit.DAYS.toMillis(value)
    }
    this.time = time

    return this
}

fun Date.humanizeDiff(): String {
    val isFuture: Boolean
    val dateDiff: Long
    (Date().time - this.time).also {
        isFuture = it < 0
        dateDiff = abs(it)
    }

    return when {
        dateDiff < TimeUnit.SECONDS.toMillis(1) -> "только что"
        dateDiff < TimeUnit.SECONDS.toMillis(45) ->  {
            resolvePastFutureDependentValue(isFuture, "несколько секунд")
        }
        dateDiff < TimeUnit.SECONDS.toMillis(75) -> {
            resolvePastFutureDependentValue(isFuture, "минуту")
        }
        dateDiff < TimeUnit.MINUTES.toMillis(45) -> {
            val diffInMinutes = getValueOrMin(TimeUnit.MILLISECONDS.toMinutes(dateDiff), 2)
            resolvePastFutureDependentValue(
                isFuture,
                TimeUnits.stringifyValue(TimeUnits.MINUTE, diffInMinutes)
            )
        }
        dateDiff < TimeUnit.MINUTES.toMillis(75) -> {
            resolvePastFutureDependentValue(isFuture, "час")
        }
        dateDiff < TimeUnit.HOURS.toMillis(22) -> {
            val diffInHours = getValueOrMin(TimeUnit.MILLISECONDS.toHours(dateDiff), 2)
            resolvePastFutureDependentValue(
                isFuture,
                TimeUnits.stringifyValue(TimeUnits.HOUR, diffInHours)
            )
        }
        dateDiff < TimeUnit.HOURS.toMillis(26) -> {
            resolvePastFutureDependentValue(isFuture, "день")
        }
        dateDiff < TimeUnit.DAYS.toMillis(360) -> {
            val diffInDays = getValueOrMin(TimeUnit.MILLISECONDS.toDays(dateDiff), 2)
            resolvePastFutureDependentValue(
                isFuture,
                TimeUnits.stringifyValue(TimeUnits.DAY, diffInDays)
            )
        }
        else -> if (isFuture) "более чем через год" else "более года назад"
    }
}

enum class TimeUnits {
    MILLISECOND,
    SECOND,
    MINUTE,
    HOUR,
    DAY;

    companion object {
        private val stringForms = mapOf(
            Pair(MINUTE, listOf("минут", "минуту", "минуты")),
            Pair(HOUR, listOf("часов", "час", "часа")),
            Pair(DAY, listOf("дней", "день", "дня"))
        )

        private class TimeUnitEndRange(private val value: Int) {
            fun zero() = value == 0
            fun one() = value == 1
            fun lessThenFive() = value in 2..4
            fun fiveAndMore() = value in 5..9
        }

        fun stringifyValue(timeUnit: TimeUnits, timeUnitValue: Long): String {
            val timeUnitEndRange = TimeUnitEndRange(timeUnitValue.toString().takeLast(1).toInt())
            val timeUnitString = if (stringForms.containsKey(timeUnit)) {
                when {
                    timeUnitEndRange.zero() || timeUnitEndRange.fiveAndMore() -> stringForms[timeUnit]?.get(0)
                    timeUnitEndRange.one() -> stringForms[timeUnit]?.get(1)
                    timeUnitEndRange.lessThenFive() -> stringForms[timeUnit]?.get(2)
                    else -> ""
                }
            } else ""
            return "$timeUnitValue $timeUnitString"

        }
    }
}

private fun resolvePastFutureDependentValue(futureCondition: Boolean, value: String): String {
    val futureAdverb = "через"
    val pastAdverb = "назад"
    return getValueOrEmpty(futureCondition, "$futureAdverb ") +
            "$value${getValueOrEmpty(!futureCondition, " $pastAdverb")}"
}

private fun getValueOrEmpty(condition: Boolean, value: String) = if (condition) value else ""

private fun getValueOrMin(value: Long, min: Long) = if (value < min) min else value
