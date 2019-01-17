//@file:Suppress("NOTHING_TO_INLINE")
//
//package com.lightningkite.lokalize.units
//
//inline class Length(val meters: Double) {
//    inline operator fun plus(other: Length): Length = Length(meters + other.meters)
//    inline operator fun minus(other: Length): Length = Length(meters - other.meters)
//}
//
//inline class Area(val metersSquared: Double) {
//
//}
//
//inline class Volume(val metersCubed: Double) {
//
//}
//
///*
//Velocity
//Acceleration
//Mass
//Density
//
//*/
//
//inline class UnitType(val bits: Int) {
//    /*
//        A 	ampere 	    electric current
//        K 	kelvin 	    temperature
//        s 	second 	    time
//        m 	metre 	    length
//        kg 	kilogram 	mass
//        cd 	candela 	luminous intensity
//        mol mole 	    amount of substance
//    */
//
//    val current: Int get() = (bits shr 0 and 0xF) - 0x8
//    fun current(value: Int) = ((value + 0x8) shl 0) or (bits and (0xF shl 0).inv())
//    val temperature: Int get() = (bits shr 4 and 0xF) - 0x8
//    fun temperature(value: Int) = ((value + 0x8) shl 4) or (bits and (0xF shl 4).inv())
//    val time: Int get() = (bits shr 8 and 0xF) - 0x8
//    fun time(value: Int) = ((value + 0x8) shl 8) or (bits and (0xF shl 8).inv())
//    val length: Int get() = (bits shr 12 and 0xF) - 0x8
//    fun length(value: Int) = ((value + 0x8) shl 12) or (bits and (0xF shl 12).inv())
//    val mass: Int get() = (bits shr 16 and 0xF) - 0x8
//    fun mass(value: Int) = ((value + 0x8) shl 16) or (bits and (0xF shl 16).inv())
//    val luminous: Int get() = (bits shr 20 and 0xF) - 0x8
//    fun luminous(value: Int) = ((value + 0x8) shl 20) or (bits and (0xF shl 20).inv())
//    val amount: Int get() = (bits shr 24 and 0xF) - 0x8
//    fun amount(value: Int) = ((value + 0x8) shl 24) or (bits and (0xF shl 24).inv())
//
//    companion object {
//        val Unitless = UnitType(0x08888888)
//
//        val Current = UnitType(0x08888889)
//        val Temperature = UnitType(0x08888898)
//        val Time = UnitType(0x08888988)
//        val Length = UnitType(0x08889888)
//        val Mass = UnitType(0x08898888)
//        val Luminosity = UnitType(0x08988888)
//        val Substance = UnitType(0x09888888)
//
//        /*
//hertz 	Hz 	frequency 		s−1
//newton 	N 	force, weight 		kg⋅m⋅s−2
//pascal 	Pa 	pressure, stress 	N/m2 	kg⋅m−1⋅s−2
//joule 	J 	energy, work, heat 	N⋅m = Pa⋅m3 	kg⋅m2⋅s−2
//watt 	W 	power, radiant flux 	J/s 	kg⋅m2⋅s−3
//coulomb C 	electric charge or quantity of electricity 		s⋅A
//volt 	V 	voltage (electrical potential), emf 	W/A 	kg⋅m2⋅s−3⋅A−1
//farad 	F 	capacitance 	C/V 	kg−1⋅m−2⋅s4⋅A2
//ohm 	Ω 	resistance, impedance, reactance 	V/A 	kg⋅m2⋅s−3⋅A−2
//siemens S 	electrical conductance 	Ω−1 	kg−1⋅m−2⋅s3⋅A2
//weber 	Wb 	magnetic flux 	V⋅s 	kg⋅m2⋅s−2⋅A−1
//tesla 	T 	magnetic flux density 	Wb/m2 	kg⋅s−2⋅A−1
//henry 	H 	inductance 	Wb/A 	kg⋅m2⋅s−2⋅A−2
//degree  Celsius 	°C 	temperature relative to 273.15 K 		K
//lumen 	lm 	luminous flux 	cd⋅sr 	cd
//lux 	lx 	illuminance 	lm/m2 	m−2⋅cd
//becqu 	Bq 	radioactivity (decays per unit time) 		s−1
//gray 	Gy 	absorbed dose (of ionising radiation) 	J/kg 	m2⋅s−2
//siever 	Sv 	equivalent dose (of ionising radiation) 	J/kg 	m2⋅s−2
//katal 	kat 	catalytic activity 		mol⋅s−1
//*/
//
//        val Area = UnitType(0x0888A888)
//        val Volume = UnitType(0x0888B888)
//        val Frequency = UnitType(0x08888788)
//        val Force = UnitType(0x08899688)
//        val Energy = UnitType(0x088A9688)
//        val Power = UnitType(0x088A9588)
//        val Pressure = UnitType(0x08897688)
//    }
//
//    fun mul
//}