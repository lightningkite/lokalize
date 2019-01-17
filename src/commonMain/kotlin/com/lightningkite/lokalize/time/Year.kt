package com.lightningkite.lokalize.time

inline class Year(val sinceAD: Int) {
    val isLeap get() = sinceAD % 4 == 0 && (sinceAD % 100 != 0 || sinceAD % 400 == 0)
    val days get() = if (isLeap) 366 else 365
    override fun toString(): String = sinceAD.toString()
}