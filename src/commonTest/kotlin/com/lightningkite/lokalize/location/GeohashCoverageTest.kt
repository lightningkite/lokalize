package com.lightningkite.lokalize.location

import com.lightningkite.kommon.collection.addSorted
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue
import kotlin.test.fail

class GeohashCoverageTest {

    fun checkCoverage(
            points: List<Geohash>,
            coverage: GeohashCoverage,
            latRange: ClosedRange<Double>,
            lonRange: ClosedRange<Double>
    ) {
        val latSeq = coverage.ranges.asSequence().flatMap { sequenceOf(Geohash(it.start).latitude, Geohash(it.endInclusive).latitude) }
        val lonSeq = coverage.ranges.asSequence().flatMap { sequenceOf(Geohash(it.start).longitude, Geohash(it.endInclusive).longitude) }
        val actualLatRange = latSeq.min()!!..latSeq.max()!!
        val actualLonRange = lonSeq.min()!!..lonSeq.max()!!
        val expectedResults = points.map {
            val la = it.latitude
            val lo = it.longitude
            it to (la in latRange && lo in lonRange)
        }

        var issue: String? = null
        var successes = 0
        var failuresShouldBeIncluded = 0
        var failuresShouldNotBeIncluded = 0
        for ((point, expected) in expectedResults) {
            if (expected == coverage.ranges.any { point.bits in it }) {
                successes++
            } else {
                if (expected) {
                    failuresShouldBeIncluded++
                    if (issue == null) {
                        issue = "Point ${point.latitude}, ${point.longitude} was expected to be in the coverage for latitude ${latRange} and longitude ${lonRange}, but it was otherwise.  Compare ${latRange} to ${actualLatRange}, and ${lonRange} to ${actualLonRange}"
                    }
                } else {
                    failuresShouldNotBeIncluded++
                }
            }
        }

        println("Successes: $successes")
        println("Failures, should be included: $failuresShouldBeIncluded")
        println("Failures, should NOT be included: $failuresShouldNotBeIncluded")
        println("Range comparison: ${actualLatRange} vs ${latRange}, ${actualLonRange} vs ${lonRange}")
        assertTrue(issue == null, issue)
        assertTrue(successes > failuresShouldNotBeIncluded * 8)
    }

    @Test
    fun testCoverRatio() {
        val points = ArrayList<Geohash>()
        for (lat in -85..85 step 5) {
            for (lon in -85..85 step 5) {
                points.addSorted(Geohash.fromLatLng(lat.toDouble(), lon.toDouble()))
            }
        }
        for (lowerLat in -90..80 step 5) {
            for (lowerLon in -90..80 step 5) {
                val latRange = (lowerLat.toDouble() + .01)..(lowerLat.toDouble() + 9.99)
                val lonRange = (lowerLon.toDouble() + .01)..(lowerLon.toDouble() + 9.99)
                val coverage = GeohashCoverage.coverRatio(
                        lowerLatitude = latRange.start,
                        upperLatitude = latRange.endInclusive,
                        lowerLongitude = lonRange.start,
                        upperLongitude = lonRange.endInclusive,
                        ratioBelow = 4.0
                )

                checkCoverage(
                        points = points,
                        coverage = coverage,
                        latRange = latRange,
                        lonRange = lonRange
                )
            }
        }
    }

    @Test
    fun testCoverHashes() {
        val points = ArrayList<Geohash>()
        for (lat in -85..85 step 5) {
            for (lon in -85..85 step 5) {
                points.addSorted(Geohash.fromLatLng(lat.toDouble(), lon.toDouble()))
            }
        }
        var max = 0
        var min = Int.MAX_VALUE
        for (lowerLat in -90..80 step 5) {
            for (lowerLon in -90..80 step 5) {
                val latRange = (lowerLat.toDouble() + .01)..(lowerLat.toDouble() + 9.99)
                val lonRange = (lowerLon.toDouble() + .01)..(lowerLon.toDouble() + 9.99)
                val coverage = GeohashCoverage.coverMaxHashes(
                        lowerLatitude = latRange.start,
                        upperLatitude = latRange.endInclusive,
                        lowerLongitude = lonRange.start,
                        upperLongitude = lonRange.endInclusive,
                        hashCap = 24
                )
                max = max.coerceAtLeast(coverage.ranges.size)
                min = min.coerceAtMost(coverage.ranges.size)
//                assertTrue(coverage.ranges.size <= 12)

                checkCoverage(
                        points = points,
                        coverage = coverage,
                        latRange = latRange,
                        lonRange = lonRange
                )
            }
        }
        println("Min: $min")
        println("Max: $max")
    }

    @Test
    fun testSimplify() {
        val original = GeohashCoverage(ranges = listOf(
                -16L..0L,
                1L..21L,
                32L..48L,
                49L..50L,
                52L..89L
        ), ratio = 1.5)
        val simplified = original.simplify()
        println("Original:")
        for (r in original.ranges) {
            println("${r.start} to ${r.endInclusive}")
        }
        println("Simplified:")
        for (r in simplified.ranges) {
            println("${r.start} to ${r.endInclusive}")
        }
    }

    @Test
    fun testMeasure() {
        for (bits in 2..32) {
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

        run {
            val coverage = GeohashCoverage.coverMaxHashes(
                    lowerLatitude = 0.0,
                    upperLatitude = 10.0,
                    lowerLongitude = 0.0,
                    upperLongitude = 10.0,
                    hashCap = 12
            )
            assertTrue(coverage.ranges.size <= 12, "coverage.ranges.size <= 12, ${coverage.ranges.size}")
        }

        run {
            val coverage = GeohashCoverage.coverRatio(
                    lowerLatitude = 0.0,
                    upperLatitude = 10.0,
                    lowerLongitude = 0.0,
                    upperLongitude = 10.0,
                    ratioBelow = 2.0
            )
            assertTrue(coverage.ratio <= 2.0, "coverage.ratio <= 2.0, ${coverage.ratio}")
        }
    }
}
