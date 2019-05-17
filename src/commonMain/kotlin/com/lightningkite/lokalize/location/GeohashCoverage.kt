package com.lightningkite.lokalize.location

import kotlin.math.*

class GeohashCoverage(
        val ranges: List<LongRange>,
        val ratio: Double
) {
    fun simplify(): GeohashCoverage {
        return GeohashCoverage(simplify(ranges), ratio)
    }

    companion object {

        private fun simplify(ranges: List<LongRange>): List<LongRange> {
            val sorted = ranges.sortedBy { it.start }
            val out = ArrayList<LongRange>()

            var currentStart = sorted.first().start
            var currentEndInclusive = sorted.first().endInclusive

            for (item in sorted) {
                if (item.start > currentEndInclusive + 1) {
                    //Out of bounds
                    out.add(currentStart..currentEndInclusive)
                    //Start from this range
                    currentStart = item.start
                    currentEndInclusive = item.endInclusive
                } else if (item.endInclusive > currentEndInclusive) {
                    currentEndInclusive = item.endInclusive
                }
            }
            out.add(currentStart..currentEndInclusive)
            return out
        }

        fun countHashes(
                lowerLatitude: Double,
                upperLatitude: Double,
                lowerLongitude: Double,
                upperLongitude: Double,
                bits: Int
        ): Int {
            val latDegreesPerHash = 180.0 / 2.0.pow(bits)
            val lonDegreesPerHash = 360.0 / 2.0.pow(bits)
            val height = ceil((upperLatitude - lowerLatitude) / latDegreesPerHash) + 1
            val width = ceil((upperLongitude - lowerLongitude) / lonDegreesPerHash) + 1
            return (width * height).toInt()
        }

        fun coverageRatio(
                lowerLatitude: Double,
                upperLatitude: Double,
                lowerLongitude: Double,
                upperLongitude: Double,
                bits: Int
        ): Double {
            val latDegreesPerHash = 180.0 / 2.0.pow(bits)
            val lonDegreesPerHash = 360.0 / 2.0.pow(bits)
            val height = ceil((upperLatitude - lowerLatitude) / latDegreesPerHash) + 1
            val width = ceil((upperLongitude - lowerLongitude) / lonDegreesPerHash) + 1
            val hashCount = width * height

            val areaDegrees = (upperLongitude - lowerLongitude) * (upperLatitude - lowerLatitude)
            val coverageAreaDegrees = hashCount * latDegreesPerHash * lonDegreesPerHash
            return coverageAreaDegrees / areaDegrees
        }

        interface WrappingProgression : Sequence<Int> {
            val count: Int
        }

        val IntProgression.count: Int
            get() {
                return if ((last - first) % step == 0)
                    (last - first) / step
                else
                    (last - first) / step + 1
            }

        fun wrappingProgression(from: Int, to: Int, step: Int): WrappingProgression {
            return if (from > to) {
                val partA = (from..Int.MAX_VALUE step step)
                val partB = (Int.MIN_VALUE..to step step)
                object : WrappingProgression, Sequence<Int> by (partA.asSequence() + partB.asSequence()) {
                    override val count: Int get() = partA.count + partB.count
                    override fun toString(): String = "($partA) + ($partB)"
                }
            } else {
                val part = (from..to step step)
                object : WrappingProgression, Sequence<Int> by part.asSequence() {
                    override val count: Int get() = part.count
                    override fun toString(): String = "($part)"
                }

            }
        }

        fun cover(
                lowerLatitude: Double,
                upperLatitude: Double,
                lowerLongitude: Double,
                upperLongitude: Double,
                bits: Int
        ): GeohashCoverage {
            val latDegreesPerHash = 180.0 / 2.0.pow(bits)
            val lonDegreesPerHash = 360.0 / 2.0.pow(bits)
            val bitJump = 1 shl (31 - bits)
            val lowerGeohash = Geohash.fromLatLng(lowerLatitude, lowerLongitude)
            val upperGeohash = Geohash.fromLatLng(upperLatitude, upperLongitude)

            val latBitRange = wrappingProgression(lowerGeohash.latitudeBits, upperGeohash.latitudeBits, bitJump)
            val lonBitRange = wrappingProgression(lowerGeohash.longitudeBits, upperGeohash.longitudeBits, bitJump)
            val ranges = ArrayList<LongRange>()
            for (latBits in latBitRange) {
                for (lonBits in lonBitRange) {
                    val hash = Geohash.fromLatLongBits(latBits, lonBits)
                    ranges.add(hash.range(bits))
                }
            }
            if (ranges.isEmpty()) {
                throw IllegalStateException("No ranges, bit ranges ${latBitRange} and ${lonBitRange}, step ${bitJump}; inputs ${lowerLatitude..upperLatitude} and ${lowerLongitude..upperLongitude}")
            }

            val simplifiedRanges = simplify(ranges)
            val areaDegrees = (upperLongitude - lowerLongitude) * (upperLatitude - lowerLatitude)
            val coverageAreaDegrees = simplifiedRanges.size * latDegreesPerHash * lonDegreesPerHash
            val ratio = coverageAreaDegrees / areaDegrees

            return GeohashCoverage(simplifiedRanges, ratio)
        }

        fun coverMaxHashes(
                lowerLatitude: Double,
                upperLatitude: Double,
                lowerLongitude: Double,
                upperLongitude: Double,
                hashCap: Int = 12
        ): GeohashCoverage {
            val bits = (2..32).first { countHashes(lowerLatitude, upperLatitude, lowerLongitude, upperLongitude, it) > hashCap } - 1
            return cover(lowerLatitude, upperLatitude, lowerLongitude, upperLongitude, bits)
        }

        fun coverRatio(
                lowerLatitude: Double,
                upperLatitude: Double,
                lowerLongitude: Double,
                upperLongitude: Double,
                ratioBelow: Double = 4.0
        ): GeohashCoverage {
            val bits = (2..32).first { coverageRatio(lowerLatitude, upperLatitude, lowerLongitude, upperLongitude, it) < ratioBelow }
            return cover(lowerLatitude, upperLatitude, lowerLongitude, upperLongitude, bits)
        }

        fun coverRadius(
                center: Geohash,
                radiusKm: Double,
                bits: Int
        ): GeohashCoverage = cover(
                lowerLatitude = center.latitude - radiusKm / World.latitudeDegreeKm,
                upperLatitude = center.latitude + radiusKm / World.latitudeDegreeKm,
                lowerLongitude = center.longitude - radiusKm / World.longitudeDegreeKm(center.latitude),
                upperLongitude = center.longitude + radiusKm / World.longitudeDegreeKm(center.latitude),
                bits = bits
        )

        fun coverRadiusRatio(
                center: Geohash,
                radiusKm: Double,
                ratioBelow: Double = 4.0
        ): GeohashCoverage = coverRatio(
                lowerLatitude = center.latitude - radiusKm / World.latitudeDegreeKm,
                upperLatitude = center.latitude + radiusKm / World.latitudeDegreeKm,
                lowerLongitude = center.longitude - radiusKm / World.longitudeDegreeKm(center.latitude),
                upperLongitude = center.longitude + radiusKm / World.longitudeDegreeKm(center.latitude),
                ratioBelow = ratioBelow
        )

        fun coverRadiusMaxHashes(
                center: Geohash,
                radiusKm: Double,
                hashCap: Int = 12
        ): GeohashCoverage = coverMaxHashes(
                lowerLatitude = center.latitude - radiusKm / World.latitudeDegreeKm,
                upperLatitude = center.latitude + radiusKm / World.latitudeDegreeKm,
                lowerLongitude = center.longitude - radiusKm / World.longitudeDegreeKm(center.latitude),
                upperLongitude = center.longitude + radiusKm / World.longitudeDegreeKm(center.latitude),
                hashCap = hashCap
        )
    }
}
