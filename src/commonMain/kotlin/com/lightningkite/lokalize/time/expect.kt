package com.lightningkite.lokalize.time

import com.lightningkite.lokalize.time.ShortDuration
import com.lightningkite.lokalize.time.TimeStamp


expect fun TimeStamp.Companion.now(): TimeStamp

expect fun ShortDuration.Companion.get(): ShortDuration