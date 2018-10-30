package com.lightningkite.lokalize

enum class Month(val days: Int, val daysLeap: Int = days) {
    January(31),
    February(28, 29),
    March(31),
    April(30),
    May(31),
    June(30),
    July(31),
    August(31),
    September(30),
    October(31),
    November(30),
    December(31);

    companion object {
        val monthStartInLeapYear = run {
            var dayCounter = 0
            IntArray(12) { index ->
                val result = dayCounter
                dayCounter += Month.values()[index].daysLeap
                result
            }
        }
        val monthStartInNormalYear = run {
            var dayCounter = 0
            IntArray(12) { index ->
                val result = dayCounter
                dayCounter += Month.values()[index].days
                result
            }
        }
    }

    val startDayInLeapYear: Int get() = monthStartInLeapYear[this.ordinal]
    val startDayInNormalYear: Int get() = monthStartInNormalYear[this.ordinal]
}