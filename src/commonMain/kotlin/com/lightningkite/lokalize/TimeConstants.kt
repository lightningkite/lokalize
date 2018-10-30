package com.lightningkite.lokalize

object TimeConstants {
    const val EPOCH_STARTED_ON_DAY_AD: Int = 719528


    const val MS_PER_SECOND: Long = 1000L
    const val MS_PER_MINUTE: Long = 60L * MS_PER_SECOND
    const val MS_PER_HOUR: Long = 60L * MS_PER_MINUTE
    const val MS_PER_DAY: Long = 24L * MS_PER_HOUR

    const val NS_PER_MILLISECOND: Long = 1_000_000L
    const val NS_PER_SECOND: Long = MS_PER_SECOND * NS_PER_MILLISECOND
    const val NS_PER_MINUTE: Long = MS_PER_MINUTE * NS_PER_MILLISECOND
    const val NS_PER_HOUR: Long = MS_PER_HOUR * NS_PER_MILLISECOND
    const val NS_PER_DAY: Long = MS_PER_DAY * NS_PER_MILLISECOND

    const val MS_PER_SECOND_INT: Int = 1000
    const val MS_PER_MINUTE_INT: Int = 60 * MS_PER_SECOND_INT
    const val MS_PER_HOUR_INT: Int = 60 * MS_PER_MINUTE_INT
    const val MS_PER_DAY_INT: Int = 24 * MS_PER_HOUR_INT

    const val DAYS_TO_LEAP_DAY = 31 + 28
    const val DAYS_PER_YEAR = 365
    const val DAYS_PER_4_YEARS = DAYS_PER_YEAR * 4 + 1
    const val DAYS_PER_100_YEARS = DAYS_PER_4_YEARS * 25 - 1
    const val DAYS_PER_400_YEARS = DAYS_PER_100_YEARS * 4 + 1
}