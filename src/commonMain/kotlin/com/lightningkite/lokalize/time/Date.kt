package com.lightningkite.lokalize.time

inline class Date(val daysSinceEpoch: Int) : Comparable<Date> {

    constructor(
            year: Year,
            month: Month,
            day: Int
    ) : this(
            -TimeConstants.EPOCH_STARTED_ON_DAY_AD +
                    1 + //Leap day in year zero
                    year.sinceAD * TimeConstants.DAYS_PER_YEAR +
                    year.sinceAD / 4 -
                    year.sinceAD / 100 +
                    year.sinceAD / 400 +
                    (if (year.isLeap) -1 else 0) +
                    (if (year.isLeap) month.startDayInLeapYear else month.startDayInNormalYear) +
                    (day - 1) //Need to get day back to being zero-indexed
    )

    override fun compareTo(other: Date): Int = daysSinceEpoch.compareTo(other.daysSinceEpoch)

    companion object {
        fun iso8601(string: String): Date = Date(
                year = Year(string.substringBefore('-').toIntOrNull() ?: 1970),
                month = Month.values()[string.substringAfter('-').substringBefore('-').toIntOrNull()?.minus(1) ?: 0],
                day = string.substringAfterLast('-').toIntOrNull() ?: 0
        )

        val MIN = Date(Int.MIN_VALUE)
        val MAX = Date(Int.MAX_VALUE)

    }

    private val yearAndDayInYear: YearAndDayInYear
        get() {
            var remainingDays = daysSinceEpoch + TimeConstants.EPOCH_STARTED_ON_DAY_AD - 1

            val setsOf400Years = remainingDays / TimeConstants.DAYS_PER_400_YEARS
            remainingDays -= setsOf400Years * TimeConstants.DAYS_PER_400_YEARS
            val setsOf100Years = remainingDays / TimeConstants.DAYS_PER_100_YEARS
            remainingDays -= setsOf100Years * TimeConstants.DAYS_PER_100_YEARS
            val setsOf4Years = remainingDays / TimeConstants.DAYS_PER_4_YEARS
            remainingDays -= setsOf4Years * TimeConstants.DAYS_PER_4_YEARS
            val extraYears = remainingDays / TimeConstants.DAYS_PER_YEAR
            remainingDays -= extraYears * TimeConstants.DAYS_PER_YEAR

            val year = Year(setsOf400Years * 400 + setsOf100Years * 100 + setsOf4Years * 4 + extraYears)

            val daysIntoYear = if (year.isLeap && extraYears != 4 && setsOf4Years != 25 && setsOf100Years != 4) remainingDays + 1 else remainingDays
            return YearAndDayInYear.make(year, daysIntoYear)
        }

    @Suppress("NOTHING_TO_INLINE")
    private inline infix fun Int.remPositive(other: Int): Int = this.rem(other).plus(other).rem(other)

    val dayOfWeek: DayOfWeek get() = DayOfWeek.values()[(daysSinceEpoch + 4) remPositive 7]
    fun toNextDayOfWeek(value: DayOfWeek): Date {
        val current = dayOfWeek
        return Date(daysSinceEpoch + ((value.ordinal + 7 - current.ordinal) remPositive 7))
    }

    fun toDayInSameWeek(value: DayOfWeek): Date {
        val current = dayOfWeek
        return Date(daysSinceEpoch + value.ordinal - current.ordinal)
    }

    val dayOfYear: Int get() = yearAndDayInYear.dayInYear
    fun toDayInSameYear(value: Int): Date {
        val current = dayOfYear
        return Date(daysSinceEpoch + value - current)
    }

    val dayOfMonth: Int get() = yearAndDayInYear.dayOfMonth

    fun toDayInMonth(value: Int): Date {
        val current = dayOfMonth
        return Date(daysSinceEpoch + value - current)
    }

    val month: Month get() = yearAndDayInYear.month

    fun toMonthInYear(value: Month): Date {
        val year = year
        return Date(year, value, dayOfMonth.coerceAtMost(if (year.isLeap) value.daysLeap else value.days))
    }

    val year: Year get() = yearAndDayInYear.year

    fun iso8601(): String = "${year.sinceAD.toString().padStart(4, '0')}-${month.ordinal.plus(1).toString().padStart(2, '0')}-${dayOfMonth.toString().padStart(2, '0')}"

    operator fun minus(other: Date) = Duration((daysSinceEpoch - other.daysSinceEpoch).times(TimeConstants.MS_PER_DAY))
}
