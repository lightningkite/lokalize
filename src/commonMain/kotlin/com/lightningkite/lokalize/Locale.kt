package com.lightningkite.lokalize

import com.lightningkite.lokalize.time.*


interface Locale{

    val language: String
    val languageVariant: String
    fun getTimeOffsetMilliseconds():Long
    fun renderNumber(value: Number, decimalPositions: Int, maxOtherPositions: Int):String
    fun renderDate(date: Date):String
    fun renderTime(time: Time):String
    fun renderDateTime(dateTime: DateTime):String
    fun renderTimeStamp(timeStamp: TimeStamp):String

    companion object {
        val default: Locale get() = DefaultLocale
    }
}

fun Locale.getTimeOffset(): Duration = Duration(getTimeOffsetMilliseconds())
