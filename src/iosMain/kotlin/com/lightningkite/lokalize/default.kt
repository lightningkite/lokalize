package com.lightningkite.lokalize

import platform.Foundation.*
import platform.QuartzCore.CACurrentMediaTime
import platform.posix.gettimeofday

@SharedImmutable
actual val DefaultLocale = object : Locale {

    override val language: String
        get() = NSBundle.mainBundle.preferredLocalizations.firstOrNull().let { it as? String ?: "en" }.substringBefore('-')
    override val languageVariant: String
        get() = NSBundle.mainBundle.preferredLocalizations.firstOrNull().let { it as? String ?: "US" }.substringAfter('-')

    override fun getTimeOffsetMilliseconds(): Long = NSTimeZone.localTimeZone.secondsFromGMT.times(1000)

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
        dateFormatter.setDateStyle(NSDateFormatterMediumStyle)
        dateFormatter.setTimeStyle(NSDateFormatterNoStyle)
        return NSDate.dateWithTimeIntervalSince1970(
                secs = TimeStamp(date = date, time = Time(0)).millisecondsSinceEpoch.times(1000.0)
        ).let{ dateFormatter.stringFromDate(it) }
    }

    override fun renderTime(time: Time): String {
        val dateFormatter = NSDateFormatter()
        dateFormatter.setDateStyle(NSDateFormatterNoStyle)
        dateFormatter.setTimeStyle(NSDateFormatterMediumStyle)
        return NSDate.dateWithTimeIntervalSince1970(
                secs = time.millisecondsSinceMidnight.times(1000.0)
        ).let{ dateFormatter.stringFromDate(it) }
    }

    override fun renderDateTime(dateTime: DateTime): String {
        val dateFormatter = NSDateFormatter()
        dateFormatter.setDateStyle(NSDateFormatterMediumStyle)
        dateFormatter.setTimeStyle(NSDateFormatterMediumStyle)
        return NSDate.dateWithTimeIntervalSince1970(
                secs = dateTime.toTimeStamp().millisecondsSinceEpoch.times(1000.0)
        ).let{ dateFormatter.stringFromDate(it) }
    }

    override fun renderTimeStamp(timeStamp: TimeStamp): String {
        val dateFormatter = NSDateFormatter()
        dateFormatter.setDateStyle(NSDateFormatterMediumStyle)
        dateFormatter.setTimeStyle(NSDateFormatterMediumStyle)
        return NSDate.dateWithTimeIntervalSince1970(
                secs = timeStamp.millisecondsSinceEpoch.times(1000.0)
        ).let{ dateFormatter.stringFromDate(it) }
    }
}

actual fun TimeStamp.Companion.now(): TimeStamp {
    return TimeStamp(NSDate.date().timeIntervalSince1970.times(1000).toLong())
}

actual fun ShortDuration.Companion.get(): ShortDuration = ShortDuration(CACurrentMediaTime().times(TimeConstants.NS_PER_SECOND).toLong())
