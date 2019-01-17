package com.lightningkite.lokalize.location

import com.lightningkite.kommon.collection.addSorted
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.fail

class GeohashCoverageTest {

    @Test
    fun testCoverRatio() {
        val points = ArrayList<Geohash>()
        for (lat in -90..90) {
            for (lon in -90..90) {
                points.addSorted(Geohash.fromLatLng(lat.toDouble(), lon.toDouble()))
            }
        }
        for (lowerLat in -90..80 step 5) {
            for (lowerLon in -90..80 step 5) {
                val coverage = GeohashCoverage.coverRatio(
                        lowerLatitude = lowerLat.toDouble(),
                        upperLatitude = lowerLat + 10.0,
                        lowerLongitude = lowerLon.toDouble(),
                        upperLongitude = lowerLon + 10.0,
                        ratioBelow = 4.0
                ).simplify()

                val shouldMatch = points.filter {
                    val la = it.latitude
                    val lo = it.longitude
                    lowerLat < la && la < lowerLat + 10 && lowerLon < lo && lo < lowerLon + 10
                }.toSet()

                val matching = ArrayList<Geohash>()
                for (range in coverage.ranges) {
                    matching.addAll(points.filter { it.bits in range })
                }
                val matchingSet = matching.map { it.toString() }.toSet()

                for(thing in shouldMatch){
                    assertTrue(thing.toString() in matchingSet, "${thing.bits} not in matching set")
                }
            }
        }
    }

    @Test
    fun testCoverHashes() {
        val points = ArrayList<Geohash>()
        for (lat in -90..90) {
            for (lon in -90..90) {
                points.addSorted(Geohash.fromLatLng(lat.toDouble(), lon.toDouble()))
            }
        }
        for (lowerLat in -90..80 step 5) {
            for (lowerLon in -90..80 step 5) {
                val coverage = GeohashCoverage.coverMaxHashes(
                        lowerLatitude = lowerLat.toDouble(),
                        upperLatitude = lowerLat + 10.0,
                        lowerLongitude = lowerLon.toDouble(),
                        upperLongitude = lowerLon + 10.0,
                        hashCap = 12
                ).simplify()

                val shouldMatch = points.filter {
                    val la = it.latitude
                    val lo = it.longitude
                    lowerLat < la && la < lowerLat + 10 && lowerLon < lo && lo < lowerLon + 10
                }.toSet()

                val matching = ArrayList<Geohash>()
                for (range in coverage.ranges) {
                    matching.addAll(points.filter { it.bits in range })
                }
                val matchingSet = matching.map { it.toString() }.toSet()

                for(thing in shouldMatch){
                    assertTrue(thing.toString() in matchingSet, "${thing.bits} not in matching set")
                }
            }
        }
    }

    @Test
    fun testMeasure() {
        for(bits in 2 .. 32){
            println("bits: $bits")
            val x = GeohashCoverage.countHashes(
                    lowerLatitude = 0.0,
                    upperLatitude = 10.0,
                    lowerLongitude = 0.0,
                    upperLongitude = 10.0,
                    bits = bits
            )
            println("hashes: $x")
            val c = GeohashCoverage.coverageRatio(
                    lowerLatitude = 0.0,
                    upperLatitude = 10.0,
                    lowerLongitude = 0.0,
                    upperLongitude = 10.0,
                    bits = bits
            )
            println("coverage: $c")
        }

        println("Hashes less than 12: " + GeohashCoverage.coverMaxHashes(
                lowerLatitude = 0.0,
                upperLatitude = 10.0,
                lowerLongitude = 0.0,
                upperLongitude = 10.0,
                hashCap = 12
        ).ranges.size)

        println("Ratio less than 2.0: " + GeohashCoverage.coverRatio(
                lowerLatitude = 0.0,
                upperLatitude = 10.0,
                lowerLongitude = 0.0,
                upperLongitude = 10.0,
                ratioBelow = 2.0
        ).ratio)

        fail()
    }
}