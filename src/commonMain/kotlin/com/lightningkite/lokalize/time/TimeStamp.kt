package com.lightningkite.lokalize.time

import com.lightningkite.lokalize.DefaultLocale


inline class TimeStamp(val millisecondsSinceEpoch: Long) : Comparable<TimeStamp> {

    override fun compareTo(other: TimeStamp): Int = millisecondsSinceEpoch.compareTo(other.millisecondsSinceEpoch)

    companion object {
        fun iso8601(string: String): TimeStamp {
            return TimeStamp(
                date = Date.iso8601(string.substringBefore('T')),
                time = Time.iso8601(string.substringAfterLast('T').substringBefore('Z')),
                offset = Duration.zero
            )
        }

        val MIN = TimeStamp(Long.MIN_VALUE)
        val MAX = TimeStamp(Long.MAX_VALUE)
    }

    fun iso8601(): String = date(Duration.zero).iso8601() + "T" + time(Duration.zero).iso8601() + "Z"

    operator fun plus(duration: Duration) = TimeStamp(millisecondsSinceEpoch + duration.milliseconds)
    operator fun minus(duration: Duration) = TimeStamp(millisecondsSinceEpoch - duration.milliseconds)
    operator fun minus(other: TimeStamp) = Duration(millisecondsSinceEpoch - other.millisecondsSinceEpoch)

    fun date(offset: Duration = Duration(DefaultLocale.getTimeOffsetMilliseconds())): Date =
            Date(((millisecondsSinceEpoch - offset.milliseconds) / TimeConstants.MS_PER_DAY).toInt())

    fun time(offset: Duration = Duration(DefaultLocale.getTimeOffsetMilliseconds())): Time =
            Time(((millisecondsSinceEpoch - offset.milliseconds) % TimeConstants.MS_PER_DAY).toInt())

    fun dateTime(offset: Duration = Duration(DefaultLocale.getTimeOffsetMilliseconds())): DateTime = DateTime(date(offset), time(offset))


    constructor(
            date: Date,
            time: Time,
            offset: Duration = Duration(DefaultLocale.getTimeOffsetMilliseconds())
    ) : this(
            date.daysSinceEpoch * 24L * 60 * 60 * 1000 +
                    time.millisecondsSinceMidnight +
                    offset.milliseconds
    )

}