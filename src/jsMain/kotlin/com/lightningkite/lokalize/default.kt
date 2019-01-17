package com.lightningkite.lokalize

import com.lightningkite.lokalize.time.*
import kotlin.browser.window

actual val DefaultLocale = object : Locale {

    override val language: String
        get() = window.navigator.language.substringBefore('-')
    override val languageVariant: String
        get() = window.navigator.language.substringAfter('-', "")

    override fun getTimeOffsetMilliseconds(): Long = kotlin.js.Date().getTimezoneOffset() * TimeConstants.MS_PER_MINUTE

    override fun renderNumber(value: Number, decimalPositions: Int, maxOtherPositions: Int): String {
        return value.asDynamic()?.toFixed(decimalPositions) as String
    }

    override fun renderDate(date: Date): String {
        return kotlin.js.Date(milliseconds = TimeStamp(date = date, time = Time(0)).millisecondsSinceEpoch).toLocaleString()
    }

    override fun renderTime(time: Time): String {
        return kotlin.js.Date(milliseconds = time.millisecondsSinceMidnight).toLocaleString()
    }

    override fun renderDateTime(dateTime: DateTime): String {
        return kotlin.js.Date(milliseconds = dateTime.toTimeStamp().millisecondsSinceEpoch).toLocaleString()
    }

    override fun renderTimeStamp(timeStamp: TimeStamp): String {
        return kotlin.js.Date(milliseconds = timeStamp.millisecondsSinceEpoch).toLocaleString()
    }
}