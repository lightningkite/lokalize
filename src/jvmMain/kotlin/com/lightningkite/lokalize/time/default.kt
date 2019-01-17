package com.lightningkite.lokalize.time

actual fun TimeStamp.Companion.now(): TimeStamp {
    return TimeStamp(java.lang.System.currentTimeMillis())
}

actual fun ShortDuration.Companion.get(): ShortDuration = ShortDuration(System.nanoTime())