package ru.skillbranch.devintensive.extensions

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.concurrent.TimeUnit.MILLISECONDS
import java.util.concurrent.TimeUnit.SECONDS
import java.util.concurrent.TimeUnit.MINUTES
import java.util.concurrent.TimeUnit.HOURS
import java.util.concurrent.TimeUnit.DAYS
import kotlin.math.abs

fun Date.format(pattern: String = "HH:mm:ss dd.MM.yy"): String {
    val dateFormat = SimpleDateFormat(pattern, Locale("ru"))

    return dateFormat.format(this)
}

fun Date.add(value: Long, units: TimeUnits): Date {
    var time = this.time

    time += when (units) {
        TimeUnits.MILLISECOND -> value
        TimeUnits.SECOND -> SECONDS.toMillis(value)
        TimeUnits.MINUTE -> MINUTES.toMillis(value)
        TimeUnits.HOUR -> HOURS.toMillis(value)
        TimeUnits.DAY -> DAYS.toMillis(value)
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
        dateDiff < SECONDS.toMillis(1) -> "только что"
        dateDiff < SECONDS.toMillis(45) ->
            resolvePastFutureDependentValue(isFuture, "несколько секунд")
        dateDiff < SECONDS.toMillis(75) ->
            resolvePastFutureDependentValue(isFuture, "минуту")
        dateDiff < MINUTES.toMillis(45) -> {
            val diffInMinutes = getValueOrMin(MILLISECONDS.toMinutes(dateDiff), 2)
            resolvePastFutureDependentValue(isFuture, TimeUnits.MINUTE.plural(diffInMinutes))
        }
        dateDiff < MINUTES.toMillis(75) ->
            resolvePastFutureDependentValue(isFuture, "час")
        dateDiff < HOURS.toMillis(22) -> {
            val diffInHours = getValueOrMin(MILLISECONDS.toHours(dateDiff), 2)
            resolvePastFutureDependentValue(isFuture, TimeUnits.HOUR.plural(diffInHours))
        }
        dateDiff < HOURS.toMillis(26) ->
            resolvePastFutureDependentValue(isFuture, "день")
        dateDiff < DAYS.toMillis(360) -> {
            val diffInDays = getValueOrMin(MILLISECONDS.toDays(dateDiff), 2)
            resolvePastFutureDependentValue(isFuture, TimeUnits.DAY.plural(diffInDays))
        }
        else -> if (isFuture) "более чем через год" else "более года назад"
    }
}

enum class TimeUnits {
    MILLISECOND {
       override fun plural(value: Long) =
           stringifyValue(value, listOf("миллисекунд", "миллисекунду", "миллисекунды"))
    },
    SECOND {
        override fun plural(value: Long) =
            stringifyValue(value, listOf("секунд", "секунду", "секунды"))
    },
    MINUTE {
        override fun plural(value: Long) =
            stringifyValue(value, listOf("минут", "минуту", "минуты"))
    },
    HOUR {
        override fun plural(value: Long) =
            stringifyValue(value, listOf("часов", "час", "часа"))
    },
    DAY {
        override fun plural(value: Long) =
            stringifyValue(value, listOf("дней", "день", "дня"))
    };

    abstract fun plural(value: Long): String


    internal fun stringifyValue(timeUnitValue: Long, stringForms: List<String>): String {
        val timeUnitString = when (timeUnitValue.toString().takeLast(1).toInt()) {
            0, in 5..9 -> stringForms[0]
            1 -> stringForms[1]
            in 2..4 -> stringForms[2]
            else -> ""
        }
        return "$timeUnitValue $timeUnitString"
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
