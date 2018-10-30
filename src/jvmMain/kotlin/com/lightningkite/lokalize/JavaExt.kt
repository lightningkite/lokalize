package com.lightningkite.lokalize

import java.util.*

fun Date.toJava(): Calendar = Calendar.getInstance().apply {
    set(Calendar.YEAR, 1970)
    set(Calendar.HOUR_OF_DAY, 6)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
    set(Calendar.DAY_OF_YEAR, 1)
    add(Calendar.DAY_OF_YEAR, daysSinceEpoch)
}

fun Time.toJava(): Calendar = GregorianCalendar().apply {
    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
    add(Calendar.MILLISECOND, millisecondsSinceMidnight)
}

fun TimeStamp.toJava(): java.util.Date = java.util.Date(millisecondsSinceEpoch)

fun DateTime.toJava(): Calendar = Calendar.getInstance().apply {
    set(Calendar.YEAR, 1970)
    set(Calendar.HOUR_OF_DAY, 6)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
    set(Calendar.DAY_OF_YEAR, 1)
    add(Calendar.DAY_OF_YEAR, date.daysSinceEpoch)

    set(Calendar.HOUR_OF_DAY, 0)
    set(Calendar.MINUTE, 0)
    set(Calendar.SECOND, 0)
    set(Calendar.MILLISECOND, 0)
    add(Calendar.MILLISECOND, this@toJava.time.millisecondsSinceMidnight)
}

fun Calendar.toTime(): Time = Time(
        hours = get(Calendar.HOUR_OF_DAY),
        minutes = get(Calendar.MINUTE),
        seconds = get(Calendar.SECOND),
        milliseconds = get(Calendar.MILLISECOND)
)

fun Calendar.toDate(): Date = Date(((timeInMillis + timeZone.rawOffset) / TimeConstants.MS_PER_DAY).toInt())

fun Calendar.toDateTime(): DateTime = DateTime(
        toDate(),
        toTime()
)