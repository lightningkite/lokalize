package com.lightningkite.lokalize.time

import com.lightningkite.lokalize.DefaultLocale
import com.lightningkite.lokalize.Locale

data class DateTime(val date: Date, val time: Time) : Comparable<DateTime> {

    override fun compareTo(other: DateTime): Int {
        val dateResult = date.compareTo(other.date)
        if (dateResult != 0) return dateResult
        return time.compareTo(other.time)
    }

    companion object {
        fun iso8601(string: String): DateTime {
            return DateTime(
                date = Date.iso8601(string.substringBefore('T')),
                time = Time.iso8601(string.substringAfterLast('T').substringBefore('+'))
            )
        }
    }

    fun toTimeStamp(offset: Duration = Duration(DefaultLocale.getTimeOffsetMilliseconds())) = TimeStamp(date, time, offset)

    fun iso8601(offset: Duration = Duration(DefaultLocale.getTimeOffsetMilliseconds())):String = date.iso8601() + "T" + time.iso8601() + "+" + offset.hours.toString().padStart(2, '0') + ":" + offset.minutes.toString().padStart(2, '0')

    operator fun minus(other: DateTime) = (date - other.date) + (time - other.time)
    override fun toString(): String = Locale.default.renderDateTime(this)
}
