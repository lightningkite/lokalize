package com.lightningkite.lokalize

import com.lightningkite.lokalize.time.*
import java.text.DateFormat
import java.text.DecimalFormat

private val javaLocale = java.util.Locale.getDefault()
private val javaTimeZone = java.util.TimeZone.getDefault()

actual val DefaultLocale = object : Locale{
    override val language: String
        get() = javaLocale.language.substringBefore('-')
    override val languageVariant: String
        get() = javaLocale.language.substringAfter('-', "")

    override fun getTimeOffsetMilliseconds(): Long = -javaTimeZone.getOffset(System.currentTimeMillis()).toLong()

    override fun renderNumber(value: Number, decimalPositions: Int, maxOtherPositions: Int): String {
        return DecimalFormat("#".repeat(maxOtherPositions) + "." + "#".repeat(decimalPositions)).format(value)
    }

    override fun renderDate(date: Date): String {
        return DateFormat.getDateInstance().format(date.toJava().time)
    }

    override fun renderTime(time: Time): String {
        return DateFormat.getTimeInstance().format(time.toJava().time)
    }

    override fun renderDateTime(dateTime: DateTime): String {
        return DateFormat.getDateTimeInstance().format(dateTime.toJava().time)
    }

    override fun renderTimeStamp(timeStamp: TimeStamp): String {
        return DateFormat.getDateTimeInstance().format(java.util.Date(timeStamp.millisecondsSinceEpoch))
    }
}