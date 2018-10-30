package com.lightningkite.lokalize

inline class ShortDuration(val nanoseconds: Long) : Comparable<ShortDuration> {

    override fun compareTo(other: ShortDuration): Int = nanoseconds.compareTo(other.nanoseconds)

    companion object {
        fun nanoseconds(nanoseconds: Long) = ShortDuration(nanoseconds)

        fun milliseconds(milliseconds: Long) = ShortDuration(milliseconds * TimeConstants.NS_PER_MILLISECOND)
        fun seconds(seconds: Long) = ShortDuration(seconds * TimeConstants.NS_PER_SECOND)
        fun minutes(seconds: Long) = ShortDuration(seconds * TimeConstants.NS_PER_MINUTE)
        fun hours(seconds: Long) = ShortDuration(seconds * TimeConstants.NS_PER_HOUR)
        fun days(seconds: Long) = ShortDuration(seconds * TimeConstants.NS_PER_DAY)

        fun seconds(seconds: Float) = ShortDuration((seconds * TimeConstants.NS_PER_SECOND).toLong())
        fun minutes(seconds: Float) = ShortDuration((seconds * TimeConstants.NS_PER_MINUTE).toLong())
        fun hours(seconds: Float) = ShortDuration((seconds * TimeConstants.NS_PER_HOUR).toLong())
        fun days(seconds: Float) = ShortDuration((seconds * TimeConstants.NS_PER_DAY).toLong())

        fun seconds(seconds: Double) = ShortDuration((seconds * TimeConstants.NS_PER_SECOND).toLong())
        fun minutes(seconds: Double) = ShortDuration((seconds * TimeConstants.NS_PER_MINUTE).toLong())
        fun hours(seconds: Double) = ShortDuration((seconds * TimeConstants.NS_PER_HOUR).toLong())
        fun days(seconds: Double) = ShortDuration((seconds * TimeConstants.NS_PER_DAY).toLong())

        inline fun measure(action: () -> Unit): ShortDuration {
            val start = ShortDuration.get()
            action()
            val end = ShortDuration.get()
            return end - start
        }
    }

    val milliseconds: Long get() = nanoseconds / TimeConstants.NS_PER_SECOND
    val seconds: Long get() = nanoseconds / TimeConstants.NS_PER_SECOND
    val minutes: Long get() = nanoseconds / TimeConstants.NS_PER_MINUTE
    val hours: Long get() = nanoseconds / TimeConstants.NS_PER_HOUR
    val days: Long get() = nanoseconds / TimeConstants.NS_PER_DAY

    val millisecondsDouble: Double get() = nanoseconds.toDouble() / TimeConstants.NS_PER_MILLISECOND
    val secondsDouble: Double get() = nanoseconds.toDouble() / TimeConstants.NS_PER_SECOND
    val minutesDouble: Double get() = nanoseconds.toDouble() / TimeConstants.NS_PER_MINUTE
    val hoursDouble: Double get() = nanoseconds.toDouble() / TimeConstants.NS_PER_HOUR
    val daysDouble: Double get() = nanoseconds.toDouble() / TimeConstants.NS_PER_DAY

    operator fun plus(other: ShortDuration) = ShortDuration(nanoseconds + other.nanoseconds)
    operator fun minus(other: ShortDuration) = ShortDuration(nanoseconds - other.nanoseconds)
    operator fun plus(other: Duration) =
        ShortDuration(nanoseconds + other.milliseconds * TimeConstants.NS_PER_MILLISECOND)

    operator fun minus(other: Duration) =
        ShortDuration(nanoseconds - other.milliseconds * TimeConstants.NS_PER_MILLISECOND)

    operator fun times(scale: Int) = ShortDuration((nanoseconds * scale))
    operator fun times(scale: Float) = ShortDuration((nanoseconds * scale).toLong())
    operator fun times(scale: Double) = ShortDuration((nanoseconds * scale).toLong())
    operator fun div(scale: Int) = ShortDuration((nanoseconds / scale))
    operator fun div(scale: Float) = ShortDuration((nanoseconds / scale).toLong())
    operator fun div(scale: Double) = ShortDuration((nanoseconds / scale).toLong())

    fun toDuration(): Duration = Duration.milliseconds(milliseconds)
}