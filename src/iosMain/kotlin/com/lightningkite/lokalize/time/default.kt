package com.lightningkite.lokalize.time

import platform.Foundation.*
import platform.QuartzCore.CACurrentMediaTime
import platform.posix.gettimeofday

actual fun TimeStamp.Companion.now(): TimeStamp {
    return TimeStamp(NSDate.date().timeIntervalSince1970.times(1000).toLong())
}

actual fun ShortDuration.Companion.get(): ShortDuration = ShortDuration(CACurrentMediaTime().times(TimeConstants.NS_PER_SECOND).toLong())
