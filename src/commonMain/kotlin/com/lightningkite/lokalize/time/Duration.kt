package com.lightningkite.lokalize.time

inline class Duration(val milliseconds: Long) : Comparable<Duration> {

    override fun compareTo(other: Duration): Int = milliseconds.compareTo(other.milliseconds)

    companion object {
        fun milliseconds(milliseconds: Long) = Duration(milliseconds)

        fun seconds(seconds: Long) = Duration(seconds * TimeConstants.MS_PER_SECOND)
        fun minutes(seconds: Long) = Duration(seconds * TimeConstants.MS_PER_MINUTE)
        fun hours(seconds: Long) = Duration(seconds * TimeConstants.MS_PER_HOUR)
        fun days(seconds: Long) = Duration(seconds * TimeConstants.MS_PER_DAY)

        fun seconds(seconds: Float) = Duration((seconds * TimeConstants.MS_PER_SECOND).toLong())
        fun minutes(seconds: Float) = Duration((seconds * TimeConstants.MS_PER_MINUTE).toLong())
        fun hours(seconds: Float) = Duration((seconds * TimeConstants.MS_PER_HOUR).toLong())
        fun days(seconds: Float) = Duration((seconds * TimeConstants.MS_PER_DAY).toLong())

        fun seconds(seconds: Double) = Duration((seconds * TimeConstants.MS_PER_SECOND).toLong())
        fun minutes(seconds: Double) = Duration((seconds * TimeConstants.MS_PER_MINUTE).toLong())
        fun hours(seconds: Double) = Duration((seconds * TimeConstants.MS_PER_HOUR).toLong())
        fun days(seconds: Double) = Duration((seconds * TimeConstants.MS_PER_DAY).toLong())

        val zero = Duration(0)
    }

    val seconds: Long get() = milliseconds / TimeConstants.MS_PER_SECOND
    val minutes: Long get() = milliseconds / TimeConstants.MS_PER_MINUTE
    val hours: Long get() = milliseconds / TimeConstants.MS_PER_HOUR
    val days: Long get() = milliseconds / TimeConstants.MS_PER_DAY

    val secondsDouble: Double get() = milliseconds.toDouble() / TimeConstants.MS_PER_SECOND
    val minutesDouble: Double get() = milliseconds.toDouble() / TimeConstants.MS_PER_MINUTE
    val hoursDouble: Double get() = milliseconds.toDouble() / TimeConstants.MS_PER_HOUR
    val daysDouble: Double get() = milliseconds.toDouble() / TimeConstants.MS_PER_DAY

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
}
