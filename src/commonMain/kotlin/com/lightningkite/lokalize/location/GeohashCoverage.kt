package com.lightningkite.lokalize.location

import kotlin.math.ceil
import kotlin.math.pow

class GeohashCoverage(
        val ranges: List<LongRange>,
        val ratio: Double
) {
    fun simplify(): GeohashCoverage {
        val sorted = ranges.sortedBy { it.start }
        val out = ArrayList<LongRange>()
        var currentStart = 0L
        var currentEndInclusive = 0L
        for(item in sorted){
            if(currentStart == 0L) {
                currentStart = item.start
            }
            currentEndInclusive = item.endInclusive
            if(currentEndInclusive + 1 != item.start) {
                out.add(currentStart .. currentEndInclusive)
                currentStart = 0
            }
        }
        return GeohashCoverage(out, ratio)
    }

    companion object {

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

            val areaDegrees = (upperLongitude - lowerLongitude) * (upperLatitude - lowerLatitude)
            val coverageAreaDegrees = width * height * latDegreesPerHash * lonDegreesPerHash
            return coverageAreaDegrees / areaDegrees
        }

        fun cover(
                lowerLatitude: Double,
                upperLatitude: Double,
                lowerLongitude: Double,
                upperLongitude: Double,
                bits: Int
        ): GeohashCoverage {
            val ranges = ArrayList<LongRange>()
            val latDegreesPerHash = 180.0 / 2.0.pow(bits)
            val lonDegreesPerHash = 360.0 / 2.0.pow(bits)

            var currentLat = lowerLatitude
            var currentLon = lowerLongitude
            while (currentLat < upperLatitude + latDegreesPerHash) {
                while (currentLon < upperLongitude + lonDegreesPerHash) {
                    ranges.add(Geohash.fromLatLng(currentLat, currentLon).range(bits))
                    currentLon += lonDegreesPerHash
                }
                currentLon = lowerLongitude
                currentLat += latDegreesPerHash
            }

            val areaDegrees = (upperLongitude - lowerLongitude) * (upperLatitude - lowerLatitude)
            val coverageAreaDegrees = ranges.size * latDegreesPerHash * lonDegreesPerHash
            val ratio = coverageAreaDegrees / areaDegrees

            return GeohashCoverage(ranges, ratio)
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
                lowerLatitude = center.latitude - radiusKm * World.latitudeDegreeKm,
                upperLatitude = center.latitude + radiusKm * World.latitudeDegreeKm,
                lowerLongitude = center.longitude - radiusKm * World.longitudeDegreeKm(center.latitude),
                upperLongitude = center.longitude + radiusKm * World.longitudeDegreeKm(center.latitude),
                bits = bits
        )

        fun coverRadiusRatio(
                center: Geohash,
                radiusKm: Double,
                ratioBelow: Double = 4.0
        ): GeohashCoverage = coverRatio(
                lowerLatitude = center.latitude - radiusKm * World.latitudeDegreeKm,
                upperLatitude = center.latitude + radiusKm * World.latitudeDegreeKm,
                lowerLongitude = center.longitude - radiusKm * World.longitudeDegreeKm(center.latitude),
                upperLongitude = center.longitude + radiusKm * World.longitudeDegreeKm(center.latitude),
                ratioBelow = ratioBelow
        )

        fun coverRadiusMaxHashes(
                center: Geohash,
                radiusKm: Double,
                hashCap: Int = 12
        ): GeohashCoverage = coverMaxHashes(
                lowerLatitude = center.latitude - radiusKm * World.latitudeDegreeKm,
                upperLatitude = center.latitude + radiusKm * World.latitudeDegreeKm,
                lowerLongitude = center.longitude - radiusKm * World.longitudeDegreeKm(center.latitude),
                upperLongitude = center.longitude + radiusKm * World.longitudeDegreeKm(center.latitude),
                hashCap = hashCap
        )
    }
}
