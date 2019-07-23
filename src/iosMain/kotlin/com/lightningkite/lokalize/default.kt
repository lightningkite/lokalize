package com.lightningkite.lokalize

import com.lightningkite.lokalize.time.*
import platform.Foundation.*
import platform.QuartzCore.CACurrentMediaTime
import platform.posix.gettimeofday
import kotlin.native.concurrent.SharedImmutable

@SharedImmutable
actual val DefaultLocale = object : Locale {

    override val language: String
        get() = NSBundle.mainBundle.preferredLocalizations.firstOrNull().let { it as? String ?: "en" }.substringBefore('-')
    override val languageVariant: String
        get() = NSBundle.mainBundle.preferredLocalizations.firstOrNull().let { it as? String ?: "US" }.substringAfter('-')

    override fun getTimeOffsetMilliseconds(): Long = NSTimeZone.localTimeZone.secondsFromGMT.toLong().times(1000)

    override fun renderNumber(value: Number, decimalPositions: Int, maxOtherPositions: Int): String {
        val string = value.toString()
        if(string.contains('.')){
            return string.substringBefore('.') + "." + string.substringAfter('.').take(decimalPositions)
        } else {
            return string
        }
    }

    override fun renderDate(date: Date): String {
        val dateFormatter = NSDateFormatter()
        dateFormatter.setDateStyle(NSDateFormatterShortStyle)
        dateFormatter.setTimeStyle(NSDateFormatterNoStyle)
        return NSDate.dateWithTimeIntervalSince1970(
                secs = TimeStamp(date = date, time = Time(0)).millisecondsSinceEpoch.div(1000.0)
        ).let{ dateFormatter.stringFromDate(it) }
    }

    override fun renderTime(time: Time): String {
        val dateFormatter = NSDateFormatter()
        dateFormatter.setDateStyle(NSDateFormatterNoStyle)
        dateFormatter.setTimeStyle(NSDateFormatterShortStyle)
        return NSDate.dateWithTimeIntervalSince1970(
                secs = TimeStamp(date = TimeStamp.now().date(), time = time).millisecondsSinceEpoch.div(1000.0)
        ).let{ dateFormatter.stringFromDate(it) }
    }

    override fun renderDateTime(dateTime: DateTime): String {
        val dateFormatter = NSDateFormatter()
        dateFormatter.setDateStyle(NSDateFormatterShortStyle)
        dateFormatter.setTimeStyle(NSDateFormatterShortStyle)
        return NSDate.dateWithTimeIntervalSince1970(
                secs = dateTime.toTimeStamp().millisecondsSinceEpoch.div(1000.0)
        ).let{ dateFormatter.stringFromDate(it) }
    }

    override fun renderTimeStamp(timeStamp: TimeStamp): String {
        val dateFormatter = NSDateFormatter()
        dateFormatter.setDateStyle(NSDateFormatterShortStyle)
        dateFormatter.setTimeStyle(NSDateFormatterShortStyle)
        return NSDate.dateWithTimeIntervalSince1970(
                secs = timeStamp.millisecondsSinceEpoch.div(1000.0)
        ).let{ dateFormatter.stringFromDate(it) }
    }
}
