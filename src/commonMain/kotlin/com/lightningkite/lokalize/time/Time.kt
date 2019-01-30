package com.lightningkite.lokalize.time


inline class Time(val millisecondsSinceMidnight: Int) : Comparable<Time> {

    override fun compareTo(other: Time): Int = millisecondsSinceMidnight.compareTo(other.millisecondsSinceMidnight)

    companion object {
        fun iso8601(string: String): Time {
            val parts = string.split(':', '.')
            return Time(
                hours = parts.getOrNull(0)?.toIntOrNull() ?: 0,
                minutes = parts.getOrNull(1)?.toIntOrNull() ?: 0,
                seconds = parts.getOrNull(2)?.toIntOrNull() ?: 0,
                milliseconds = parts.getOrNull(3)?.toIntOrNull() ?: 0
            )
        }
    }

    val hours:Int
        get() = millisecondsSinceMidnight / TimeConstants.MS_PER_HOUR_INT
    val minutes:Int
        get() = millisecondsSinceMidnight / TimeConstants.MS_PER_MINUTE_INT % 60
    val seconds:Int
        get() = millisecondsSinceMidnight / TimeConstants.MS_PER_SECOND_INT % 60
    val milliseconds:Int
        get() = millisecondsSinceMidnight % TimeConstants.MS_PER_SECOND_INT

    operator fun plus(amount: Duration) = Time(millisecondsSinceMidnight + amount.milliseconds.toInt())
    operator fun minus(amount: Duration) = Time(millisecondsSinceMidnight - amount.milliseconds.toInt())
    operator fun minus(other: Time) = Duration((millisecondsSinceMidnight - other.millisecondsSinceMidnight).toLong())

    fun iso8601(): String = "${hours.toString().padStart(2, '0')}:${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}.${milliseconds.toString().padStart(2, '0')}"
}

fun Time(
        hours:Int,
        minutes:Int,
        seconds:Int = 0,
        milliseconds:Int = 0
): Time = Time(
        hours * TimeConstants.MS_PER_HOUR_INT +
                minutes * TimeConstants.MS_PER_MINUTE_INT +
                seconds * TimeConstants.MS_PER_SECOND_INT +
                milliseconds
)
