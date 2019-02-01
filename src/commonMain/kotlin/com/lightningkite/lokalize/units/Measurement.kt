//package com.lightningkite.lokalize.units
//
//interface Measurements {
//    companion object {
//        objec
//    }
//}
//
//interface MeasurementSet {
//    val measurements: Map<Measurement.Type, List<Measurement>>
//    fun express(type: Measurement.Type, value: Double): String {
//        fun
//    }
//
//    object Base {
//        val meter = Measurement(type = Measurement.Type.Length, fullName = "meters", shortName = "m")
//        val area = meter.squared()
//        val volume = meter.cubed()
//        val second = Measurement(type = Measurement.Type.Time, fullName = "seconds", shortName = "s")
//        val kilogram = Measurement(type = Measurement.Type.Mass, fullName = "kilograms", shortName = "kg")
//    }
//
//    object EnglishMetric : MeasurementSet {
//
//        val kilometer = Measurement(multiplierFromBase = 1000.0, fullName = "kilometers", shortName = "km")
//        val meter = Measurement(fullName = "meters", shortName = "m")
//        val second = Measurement(fullName = "seconds", shortName = "s")
//        val litre = Measurement(multiplierFromBase = 0.001, fullName = "litres", shortName = "L")
//
//        override val length: List<Measurement> = listOf(meter, kilometer)
//        override val area: List<Measurement> = listOf(meter.squared(), kilometer.squared())
//        override val volume: List<Measurement> = listOf()
//        override val force: List<Measurement>
//            get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
//        override val time: List<Measurement>
//            get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
//        override val speed: List<Measurement>
//            get() = TODO("not implemented") //To change initializer of created properties use File | Settings | File Templates.
//
//    }
//}
//
//class Measurement(
//        val type: Type,
//        val multiplierFromBase: Double = 1.0,
//        val fullName: String,
//        val shortName: String
//){
//    enum class Type {
//        Length,
//        Time,
//        Area,
//        Mass,
//        Force,
//        Speed
//    }
//}
//
//fun Measurement.squared(): Measurement = Measurement(
//        multiplierFromBase = multiplierFromBase * multiplierFromBase,
//        fullName = "$fullName squared",
//        shortName = "$shortName²"
//)
//
//fun Measurement.cubed(): Measurement = Measurement(
//        multiplierFromBase = multiplierFromBase * multiplierFromBase,
//        fullName = "$fullName cubed",
//        shortName = "$shortName³"
//)
//
//inline fun Measurement.per(other:Measurement) = Measurement(
//        multiplierFromBase = multiplierFromBase / other.multiplierFromBase,
//        fullName = "$fullName per ${other.fullName}",
//        shortName = "$shortName/${other.shortName}"
//)