package com.lightningkite.lokalize.time

import kotlin.browser.window

actual fun TimeStamp.Companion.now(): TimeStamp {
    return TimeStamp(kotlin.js.Date.now().toLong())
}

actual fun ShortDuration.Companion.get(): ShortDuration = ShortDuration(TimeStamp.now().millisecondsSinceEpoch.times(TimeConstants.NS_PER_MILLISECOND))