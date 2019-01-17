package com.lightningkite.lokalize.location

import com.lightningkite.kommon.bytes.bitHigh
import com.lightningkite.kommon.bytes.bitHighOff
import com.lightningkite.kommon.bytes.bitHighOn
import com.lightningkite.kommon.bytes.bitHighSet


inline class Geohash(val bits: Long) : Comparable<Geohash> {
    override fun compareTo(other: Geohash): Int {
        return bits.compareTo(other.bits)
    }

    companion object {
        val chars = charArrayOf(
                '0', '1', '2', '3', '4', '5', '6', '7',
                '8', '9', 'b', 'c', 'd', 'e', 'f', 'g',
                'h', 'j', 'k', 'm', 'n', 'p', 'q', 'r',
                's', 't', 'u', 'v', 'w', 'x', 'y', 'z'
        )

        @Suppress("NOTHING_TO_INLINE")
        inline fun fromChar(char: Char): Int {
            if (char <= '9') return char - '0'
            else if (char < 'j') return char - 'b' + 10
            else if (char < 'm') return char - 'j' + 17
            else if (char < 'p') return char - 'm' + 19
            else return char - 'p' + 21
        }

        fun fromLatLng(latitude: Double, longitude: Double): Geohash {
            var bits = 0L

            var lower = -90.0
            var upper = 90.0
            for (latBit in 0..31) {
                val middle = (upper + lower) / 2
                if (latitude >= middle) {
                    bits = bits.bitHighOn(latBit * 2 + 1)
                    lower = middle
                } else {
                    bits = bits.bitHighOff(latBit * 2 + 1)
                    upper = middle
                }
            }

            lower = -180.0
            upper = 180.0
            for (lonBit in 0..31) {
                val middle = (upper + lower) / 2
                if (longitude >= middle) {
                    bits = bits.bitHighOn(lonBit * 2)
                    lower = middle
                } else {
                    bits = bits.bitHighOff(lonBit * 2)
                    upper = middle
                }
            }

            return Geohash(bits)
        }

        fun fromString(string: String): Geohash {
            var bits = 0L
            var position = 59
            var stringIndex = 0
            for (c in string) {
                bits = bits or fromChar(c).toLong().shl(position)
                position -= 5
                stringIndex++
                if (position < 0) break
            }
            if (stringIndex < string.length) {
                println("Read from last char ${string[stringIndex]}: ${fromChar(string[stringIndex]).toLong().shr(1)}")
                bits = bits or fromChar(string[stringIndex]).toLong().shr(1)
            }
            return Geohash(bits)
        }

        fun fromLatLongBits(latitude: Int, longitude: Int): Geohash {
            var bits = 0L
            for (index in 0..31) {
                if (longitude.bitHigh(index)) {
                    bits = bits.bitHighOn(index * 2)
                }
                if (latitude.bitHigh(index)) {
                    bits = bits.bitHighOn(index * 2 + 1)
                }
            }
            return Geohash(bits)
        }
    }

    val latitude: Double
        get() {
            var lower = -90.0
            var upper = 90.0
            for (latBit in 0..31) {
                val middle = (upper + lower) / 2
                if (bits.bitHigh(latBit * 2 + 1)) {
                    lower = middle
                } else {
                    upper = middle
                }
            }
            return lower
        }

    val longitude: Double
        get() {
            var lower = -180.0
            var upper = 180.0
            for (lonBit in 0..31) {
                val middle = (upper + lower) / 2
                if (bits.bitHigh(lonBit * 2)) {
                    lower = middle
                } else {
                    upper = middle
                }
            }
            return lower
        }

    override fun toString(): String {
        val builder = StringBuilder()
        for (i in 59 downTo 0 step 5) {
            builder.append(chars[bits.ushr(i).toInt().and(0x1F)])
        }
        builder.append(chars[((bits shl 1) and 0x1F).toInt()])
        return builder.toString()
    }

    val latitudeBits: Int
        get() {
            var partialBits = 0
            for (index in 0..31) {
                partialBits = partialBits.bitHighSet(index, bits.bitHigh(index * 2 + 1))
            }
            return partialBits
        }

    val longitudeBits: Int
        get() {
            var partialBits = 0
            for (index in 0..31) {
                partialBits = partialBits.bitHighSet(index, bits.bitHigh(index * 2))
            }
            return partialBits
        }

    fun applyLatitudeBits(latitudeBits: Int): Geohash {
        var newBits = bits
        for (index in 0..31) {
            newBits = newBits.bitHighSet(index * 2 + 1, latitudeBits.bitHigh(index))
        }
        return Geohash(newBits)
    }

    fun applyLongitudeBits(longitudeBits: Int): Geohash {
        var newBits = bits
        for (index in 0..31) {
            newBits = newBits.bitHighSet(index * 2, longitudeBits.bitHigh(index))
        }
        return Geohash(newBits)
    }

    fun north(bitsResolution: Int) = applyLatitudeBits(latitudeBits + (0x1 shl (31 - bitsResolution)))
    fun south(bitsResolution: Int) = applyLatitudeBits(latitudeBits - (0x1 shl (31 - bitsResolution)))
    fun east(bitsResolution: Int) = applyLongitudeBits(longitudeBits + (0x1 shl (31 - bitsResolution)))
    fun west(bitsResolution: Int) = applyLongitudeBits(longitudeBits - (0x1 shl (31 - bitsResolution)))

    fun northEast(bitsResolution: Int) = Geohash.fromLatLongBits(
            latitude = latitudeBits + (0x1 shl (31 - bitsResolution)),
            longitude = longitudeBits + (0x1 shl (31 - bitsResolution))
    )

    fun northWest(bitsResolution: Int) = Geohash.fromLatLongBits(
            latitude = latitudeBits + (0x1 shl (31 - bitsResolution)),
            longitude = longitudeBits - (0x1 shl (31 - bitsResolution))
    )

    fun southEast(bitsResolution: Int) = Geohash.fromLatLongBits(
            latitude = latitudeBits - (0x1 shl (31 - bitsResolution)),
            longitude = longitudeBits + (0x1 shl (31 - bitsResolution))
    )

    fun southWest(bitsResolution: Int) = Geohash.fromLatLongBits(
            latitude = latitudeBits - (0x1 shl (31 - bitsResolution)),
            longitude = longitudeBits - (0x1 shl (31 - bitsResolution))
    )

    fun lower(bitsResolution: Int): Long {
        return bits and (0x1L.shl(64 - bitsResolution * 2).minus(1).inv())
    }
    fun upper(bitsResolution: Int): Long {
        return bits or (0x1L.shl(64 - bitsResolution * 2).minus(1))
    }
    fun range(bitsResolution: Int) = lower(bitsResolution)..upper(bitsResolution)
}

