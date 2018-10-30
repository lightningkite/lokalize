package com.lightningkite.lokalize

import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class Iso8601Test {

    @Test
    fun checkTimes(){
        for(time in 0 until 24){
            val original = Time(hours = time, minutes = 5)
            val reparsed = Time.iso8601(original.iso8601())
            assertEquals(
                original,
                reparsed,
                "${original.iso8601()} != ${reparsed.iso8601()}"
            )
        }
    }

    @Test
    fun checkTimeStamps(){

        for(time in 0 until 24){
            for (day in 0..TimeConstants.DAYS_PER_4_YEARS) {
                val original = TimeStamp(date = Date(day), time = Time(hours = time, minutes = 5))
                val reparsed = TimeStamp.iso8601(original.iso8601())
                assertEquals(
                    original,
                    reparsed,
                    "${original.iso8601()} != ${reparsed.iso8601()}"
                )
            }
        }
    }

    @Test
    fun checkDateTimes(){

        for(time in 0 until 24){
            for (day in 0..TimeConstants.DAYS_PER_4_YEARS) {
                val original = DateTime(date = Date(day), time = Time(hours = time, minutes = 5))
                val reparsed = DateTime.iso8601(original.iso8601())
                assertEquals(
                    original,
                    reparsed,
                    "${original.iso8601()} != ${reparsed.iso8601()}"
                )
            }
        }
    }

    @Test
    fun checkAllDates() {
        for (day in 0..TimeConstants.EPOCH_STARTED_ON_DAY_AD) {
            val original = Date(day)
            val reparsed = Date.iso8601(Date(day).iso8601())
            assertEquals(
                original,
                reparsed,
                "${original.iso8601()} != ${reparsed.iso8601()}, ${original.dayOfYear} != ${reparsed.dayOfYear}"
            )
        }
    }
    
    

    @Test
    fun enterLeapYearParse() {
        val days = listOf(
            Date.iso8601("1971-12-31"),
            Date.iso8601("1972-01-01"),
            Date.iso8601("1972-01-02")
        )
        println(days.joinToString())
        assertTrue {
            days.distinct().size == days.size
        }
    }

    @Test
    fun exitLeapYearParse() {
        val days = listOf(
            Date.iso8601("1972-12-31"),
            Date.iso8601("1973-01-01"),
            Date.iso8601("1973-01-02")
        )
        println(days.joinToString())
        assertTrue {
            days.distinct().size == days.size
        }
    }

    @Test
    fun passLeapDayParse() {
        val days = listOf(
            Date.iso8601("1972-02-28"),
            Date.iso8601("1972-02-29"),
            Date.iso8601("1972-03-01")
        )
        println(days.joinToString())
        assertTrue {
            days.distinct().size == days.size
        }
    }

    @Test
    fun passMissedLeapDayParse() {
        val days = listOf(
            Date.iso8601("1973-02-28"),
            Date.iso8601("1973-03-01")
        )
        println(days.joinToString())
        assertTrue {
            days.distinct().size == days.size
        }
    }
    
    

    @Test
    fun enterLeapYearRender() {
        val days = listOf(
            Date(729).iso8601(),
            Date(730).iso8601(),
            Date(731).iso8601(),
            Date(732).iso8601()
        )
        println(days.joinToString())
        assertTrue {
            days.distinct().size == days.size
        }
    }

    @Test
    fun exitLeapYearRender() {
        val days = listOf(
            Date(1095).iso8601(),
            Date(1096).iso8601(),
            Date(1097).iso8601()
        )
        println(days.joinToString())
        assertTrue {
            days.distinct().size == days.size
        }
    }

    @Test
    fun passLeapDayRender() {
        val days = listOf(
            Date(788).iso8601(),
            Date(789).iso8601(),
            Date(790).iso8601()
        )
        println(days.joinToString())
        assertTrue {
            days.distinct().size == days.size
        }
    }

    @Test
    fun passMissedLeapDayRender() {
        val days = listOf(
            Date(1154).iso8601(),
            Date(1155).iso8601()
        )
        println(days.joinToString())
        assertTrue {
            days.distinct().size == days.size
        }
    }
}