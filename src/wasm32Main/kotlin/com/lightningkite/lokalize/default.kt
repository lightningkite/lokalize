package com.lightningkite.lokalize

import com.lightningkite.lokalize.time.*
import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
actual val DefaultLocale = object : Locale {

    //TODO: STUB
    override val language: String
        get() = "en"
    //TODO: STUB
    override val languageVariant: String
        get() = "US"

    //TODO: STUB
    override fun getTimeOffsetMilliseconds(): Long = 0

    override fun renderNumber(value: Number, decimalPositions: Int, maxOtherPositions: Int): String {
        val string = value.toString()
        if(string.contains('.')){
            return string.substringBefore('.') + "." + string.substringAfter('.').take(decimalPositions)
        } else {
            return string
        }
    }

    //TODO: STUB
    override fun renderDate(date: Date): String = date.iso8601()

    //TODO: STUB
    override fun renderTime(time: Time): String = time.iso8601()

    //TODO: STUB
    override fun renderDateTime(dateTime: DateTime): String = dateTime.date.iso8601() + " " + dateTime.time.iso8601()

    //TODO: STUB
    override fun renderTimeStamp(timeStamp: TimeStamp): String = renderDateTime(timeStamp.dateTime())
}
