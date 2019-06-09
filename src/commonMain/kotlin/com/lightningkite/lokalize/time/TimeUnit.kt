package com.lightningkite.lokalize.time

enum class TimeUnit(val milliseconds: Long) {
    Milliseconds(1),
    Seconds(TimeConstants.MS_PER_SECOND),
    Minutes(TimeConstants.MS_PER_MINUTE),
    Hours(TimeConstants.MS_PER_HOUR),
    Days(TimeConstants.MS_PER_DAY),
    Weeks(TimeConstants.MS_PER_WEEK),
    Years(TimeConstants.MS_PER_YEAR)
}