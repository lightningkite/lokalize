package com.lightningkite.lokalize.time

class DaysOfWeek(
        var sunday: Boolean = false,
        var monday: Boolean = false,
        var tuesday: Boolean = false,
        var wednesday: Boolean = false,
        var thursday: Boolean = false,
        var friday: Boolean = false,
        var saturday: Boolean = false
) {
    val setView: MutableSet<DayOfWeek> get() {
        return object : MutableSet<DayOfWeek> {
            override val size: Int get() = iterator().asSequence().count()

            override fun add(element: DayOfWeek): Boolean {
                return if(!get(element)){
                    set(element, true)
                    true
                } else {
                    false
                }
            }

            override fun addAll(elements: Collection<DayOfWeek>): Boolean = elements.any { add(it) }

            override fun clear() {
                sunday = false
                monday = false
                tuesday = false
                wednesday = false
                thursday = false
                friday = false
                saturday = false
            }

            override fun contains(element: DayOfWeek): Boolean = get(element)

            override fun containsAll(elements: Collection<DayOfWeek>): Boolean = elements.all { get(it) }

            override fun isEmpty(): Boolean =
                    !sunday &&
                            !monday &&
                            !tuesday &&
                            !wednesday &&
                            !thursday &&
                            !friday &&
                            !saturday

            override fun iterator(): MutableIterator<DayOfWeek> = object : MutableIterator<DayOfWeek> {
                var index = -1
                override fun hasNext(): Boolean {
                    while(index < 7 && !get(DayOfWeek.values()[index])){
                        index++
                    }
                    return index != 7
                }

                override fun next(): DayOfWeek {
                    while(index < 7 && !get(DayOfWeek.values()[index])){
                        index++
                    }
                    if(index == 7) throw IllegalStateException("Iterator used incorrectly")
                    return DayOfWeek.values()[index]
                }

                override fun remove() {
                    set(DayOfWeek.values()[index], false)
                }
            }

            override fun remove(element: DayOfWeek): Boolean {
                return if(get(element)){
                    set(element, false)
                    true
                } else {
                    false
                }
            }

            override fun removeAll(elements: Collection<DayOfWeek>): Boolean = elements.any { remove(it) }

            override fun retainAll(elements: Collection<DayOfWeek>): Boolean {
                var changed = false
                DayOfWeek.values().forEach {
                    if(!elements.contains(it)){
                        set(it, false)
                        changed = true
                    }
                }
                return changed
            }
        }
    }

    companion object {
        fun trueByDefault(
                sunday: Boolean = false,
                monday: Boolean = false,
                tuesday: Boolean = false,
                wednesday: Boolean = false,
                thursday: Boolean = false,
                friday: Boolean = false,
                saturday: Boolean = false
        ): DaysOfWeek = DaysOfWeek(
                sunday = sunday,
                monday = monday,
                tuesday = tuesday,
                wednesday = wednesday,
                thursday = thursday,
                friday = friday,
                saturday = saturday
        )
    }

    operator fun get(dayOfWeek: DayOfWeek): Boolean {
        return when (dayOfWeek) {
            DayOfWeek.Sunday -> sunday
            DayOfWeek.Monday -> monday
            DayOfWeek.Tuesday -> tuesday
            DayOfWeek.Wednesday -> wednesday
            DayOfWeek.Thursday -> thursday
            DayOfWeek.Friday -> friday
            DayOfWeek.Saturday -> saturday
        }
    }

    operator fun set(dayOfWeek: DayOfWeek, value: Boolean) {
        when (dayOfWeek) {
            DayOfWeek.Sunday -> sunday = value
            DayOfWeek.Monday -> monday = value
            DayOfWeek.Tuesday -> tuesday = value
            DayOfWeek.Wednesday -> wednesday = value
            DayOfWeek.Thursday -> thursday = value
            DayOfWeek.Friday -> friday = value
            DayOfWeek.Saturday -> saturday = value
        }
    }

    val ranges: List<ClosedRange<DayOfWeek>> get(){
        val output = ArrayList<ClosedRange<DayOfWeek>>()
        var lastStart: DayOfWeek? = null
        var previous: DayOfWeek? = null
        DayOfWeek.values().forEach {
            if (get(it)) {
                if (lastStart == null) {
                    lastStart = it
                }
            } else {
                if (lastStart != null && previous != null) {
                    output.add(lastStart!!..previous!!)
                }
                lastStart = null
            }
            previous = it
        }
        return output
    }

    override fun toString(): String = ranges.joinToString {
        if (it.start == it.endInclusive)
            it.start.name
        else
            it.start.name + " - " + it.endInclusive.name
    }
}