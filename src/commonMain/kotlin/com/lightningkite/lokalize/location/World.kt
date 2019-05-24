package com.lightningkite.lokalize.location

import kotlin.math.PI
import kotlin.math.cos

object World {
    const val radius = 6371.0 // Radius of the earth
    const val latitudeDegreeKm = 110.574
    fun longitudeDegreeKm(latitude: Double) = 111.320 * cos(latitude * 180.0 / PI)
}