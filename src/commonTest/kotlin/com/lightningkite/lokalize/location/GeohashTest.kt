package com.lightningkite.lokalize.location

import kotlin.math.abs
import kotlin.random.Random
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GeohashTest {

    fun check(lat: Double, lon: Double, hash: String) {
        val geohash = Geohash.fromLatLng(lat, lon)

        println(geohash.toString() + " VS " + hash)
        assertTrue(geohash.toString().startsWith(hash))

        assertTrue(abs(geohash.latitude - lat) < .0001)
        assertTrue(abs(geohash.longitude - lon) < .0001)

        val geohashCopy = Geohash.fromString(hash)
        println(geohashCopy.toString() + " VS " + hash)
        assertTrue(geohashCopy.toString().startsWith(hash))
    }

    fun fullCheck(bits: Long) {
        val geo = Geohash(bits)

        val copyBasedOnString = Geohash.fromString(geo.toString())
        assertEquals(copyBasedOnString.bits, geo.bits)
        assertEquals(copyBasedOnString.toString(), geo.toString())
        assertEquals(copyBasedOnString.latitude, geo.latitude)
        assertEquals(copyBasedOnString.longitude, geo.longitude)
        assertEquals(copyBasedOnString.hashCode(), geo.hashCode())
        assertEquals(copyBasedOnString, geo)

        val copyBasedOnLatLong = Geohash.fromLatLng(geo.latitude, geo.longitude)
        assertEquals(copyBasedOnLatLong.bits, geo.bits)
        assertEquals(copyBasedOnLatLong.toString(), geo.toString())
        assertEquals(copyBasedOnLatLong.latitude, geo.latitude)
        assertEquals(copyBasedOnLatLong.longitude, geo.longitude)
        assertEquals(copyBasedOnLatLong.hashCode(), geo.hashCode())
        assertEquals(copyBasedOnLatLong, geo)

        val copyBasedOnLatLongBits = Geohash.fromLatLongBits(geo.latitudeBits, geo.longitudeBits)
        assertEquals(copyBasedOnLatLongBits.bits, geo.bits)
        assertEquals(copyBasedOnLatLongBits.toString(), geo.toString())
        assertEquals(copyBasedOnLatLongBits.latitude, geo.latitude)
        assertEquals(copyBasedOnLatLongBits.longitude, geo.longitude)
        assertEquals(copyBasedOnLatLongBits.hashCode(), geo.hashCode())
        assertEquals(copyBasedOnLatLongBits, geo)
    }

    @Test
    fun charsCompare() {
        for(i in 0 .. 31){
            val c = Geohash.chars[i]
            val reversed = Geohash.fromChar(c)
            assertEquals(i, reversed)
        }
    }

    @Test
    fun bitsCompare() {
        val bits = 0x78AC_912B_F437_ABC1L
        val geo = Geohash(bits)
        println("String: $geo")
        val copy = Geohash.fromString(geo.toString())
        assertEquals(copy.bits, geo.bits)
        assertEquals(copy.toString(), geo.toString())
        assertEquals(copy.latitude, geo.latitude)
        assertEquals(copy.longitude, geo.longitude)
        assertEquals(copy.hashCode(), geo.hashCode())
        assertEquals(copy, geo)
    }

    @Test
    fun hardcodedGeohashCompare() {
        check(-19.226, -63.523, "6sdfd")
        check(48.669, -4.329, "gbsuv")
    }

    @Test
    fun longCheck() {
        fullCheck(0x1234_5678_90AB_CDEFL)
    }

    @Test
    fun fromLatLongBitsCheck() {
        val latBits = 0x12345678
        val lonBits = 0x09ABCDEF
        val hash = Geohash.fromLatLongBits(latBits, lonBits)
        println(lonBits.toString(2).padStart(32, '-').flatMap { listOf(it, ' ') }.joinToString(""))
        println(latBits.toString(2).padStart(32, '-').flatMap { listOf(' ', it) }.joinToString(""))
        println(hash.bits.toString(2).padStart(64, '-'))
        println(hash.longitudeBits.toString(2).padStart(32, '-').flatMap { listOf(it, ' ') }.joinToString(""))
        println(hash.latitudeBits.toString(2).padStart(32, '-').flatMap { listOf(' ', it) }.joinToString(""))
        assertEquals(latBits, hash.latitudeBits)
        assertEquals(lonBits, hash.longitudeBits)
    }

    @Test
    fun randomChecks() {
        val rand = Random(347289)
        repeat(10000){
            fullCheck(rand.nextLong())
        }
    }
}