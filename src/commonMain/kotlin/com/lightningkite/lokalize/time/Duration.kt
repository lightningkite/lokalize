package com.lightningkite.lokalize.time

import kotlin.math.abs

inline class Duration(val milliseconds: Long) : Comparable<Duration> {

    override fun compareTo(other: Duration): Int = milliseconds.compareTo(other.milliseconds)

    companion object {
        fun milliseconds(milliseconds: Long) = Duration(milliseconds)

        fun seconds(seconds: Long) = Duration(seconds * TimeConstants.MS_PER_SECOND)
        fun minutes(minutes: Long) = Duration(minutes * TimeConstants.MS_PER_MINUTE)
        fun hours(hours: Long) = Duration(hours * TimeConstants.MS_PER_HOUR)
        fun days(days: Long) = Duration(days * TimeConstants.MS_PER_DAY)

        fun seconds(seconds: Float) = Duration((seconds * TimeConstants.MS_PER_SECOND).toLong())
        fun minutes(minutes: Float) = Duration((minutes * TimeConstants.MS_PER_MINUTE).toLong())
        fun hours(hours: Float) = Duration((hours * TimeConstants.MS_PER_HOUR).toLong())
        fun days(days: Float) = Duration((days * TimeConstants.MS_PER_DAY).toLong())

        fun seconds(seconds: Double) = Duration((seconds * TimeConstants.MS_PER_SECOND).toLong())
        fun minutes(minutes: Double) = Duration((minutes * TimeConstants.MS_PER_MINUTE).toLong())
        fun hours(hours: Double) = Duration((hours * TimeConstants.MS_PER_HOUR).toLong())
        fun days(days: Double) = Duration((days * TimeConstants.MS_PER_DAY).toLong())

        val zero = Duration(0)
    }

    val seconds: Long get() = milliseconds / TimeConstants.MS_PER_SECOND
    val minutes: Long get() = milliseconds / TimeConstants.MS_PER_MINUTE
    val hours: Long get() = milliseconds / TimeConstants.MS_PER_HOUR
    val days: Long get() = milliseconds / TimeConstants.MS_PER_DAY
    val weeks: Long get() = milliseconds / TimeConstants.MS_PER_WEEK
    val years: Long get() = milliseconds / TimeConstants.MS_PER_YEAR

    val secondsDouble: Double get() = milliseconds.toDouble() / TimeConstants.MS_PER_SECOND
    val minutesDouble: Double get() = milliseconds.toDouble() / TimeConstants.MS_PER_MINUTE
    val hoursDouble: Double get() = milliseconds.toDouble() / TimeConstants.MS_PER_HOUR
    val daysDouble: Double get() = milliseconds.toDouble() / TimeConstants.MS_PER_DAY
    val weeksDouble: Double get() = milliseconds.toDouble() / TimeConstants.MS_PER_WEEK
    val yearsDouble: Double get() = milliseconds.toDouble() / TimeConstants.MS_PER_YEAR

    operator fun plus(other: ShortDuration) = Duration(milliseconds + other.milliseconds)
    operator fun minus(other: ShortDuration) = Duration(milliseconds - other.milliseconds)
    operator fun plus(other: Duration) = Duration(milliseconds + other.milliseconds)
    operator fun minus(other: Duration) = Duration(milliseconds - other.milliseconds)
    operator fun times(scale: Int) = Duration((milliseconds * scale))
    operator fun times(scale: Float) = Duration((milliseconds * scale).toLong())
    operator fun times(scale: Double) = Duration((milliseconds * scale).toLong())
    operator fun div(scale: Int) = Duration((milliseconds / scale))
    operator fun div(scale: Float) = Duration((milliseconds / scale).toLong())
    operator fun div(scale: Double) = Duration((milliseconds / scale).toLong())

    fun toShortDuration(): ShortDuration = ShortDuration.milliseconds(milliseconds)

    fun bySignificantUnit(): Pair<TimeUnit, Double> {
        val absolute = abs(milliseconds)
        for(unit in TimeUnit.values().reversed()) {
            if(absolute > unit.milliseconds){
                return unit to (milliseconds.toDouble() / unit.milliseconds)
            }
        }
        return TimeUnit.Milliseconds to milliseconds.toDouble()
    }
}
