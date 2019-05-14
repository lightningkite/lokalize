package com.lightningkite.lokalize.location

import kotlin.math.PI
import kotlin.math.cos

object World {
    val latitudeDegreeKm = 110.574
    fun longitudeDegreeKm(latitude: Double) = 111.320 * cos(latitude * 180.0 / PI)
}