package com.lightningkite.lokalize.time

internal inline class YearAndDayInYear(val combined: Long) {
    val year: Year get() = Year((combined shr 16).toInt())
    val dayInYear: Int get() = (combined and 0xFFFF).toInt()

    companion object {
        fun make(year: Year, dayInYear: Int): YearAndDayInYear {
            return YearAndDayInYear(year.sinceAD.toLong() shl 16 or dayInYear.toLong())
        }
    }
}

internal val YearAndDayInYear.month: Month get() {
    return if(year.isLeap){
        Month.values().last { dayInYear >= it.startDayInLeapYear }
    } else {
        Month.values().last { dayInYear >= it.startDayInNormalYear }
    }
}

internal val YearAndDayInYear.dayOfMonth: Int get() {
    return if(year.isLeap){
        val month = Month.values().last { dayInYear >= it.startDayInLeapYear }
        dayInYear - month.startDayInLeapYear + 1
    } else {
        val month = Month.values().last { dayInYear >= it.startDayInNormalYear }
        dayInYear - month.startDayInNormalYear + 1
    }
}