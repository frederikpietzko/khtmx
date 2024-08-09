package de.fpietzko.khtmx.core

data class InvalidTriggerException(override val message: String) : Exception(message)

data class FilterDsl internal constructor(
    internal val filters: StringBuilder = StringBuilder(),
) {
    fun ctrlKey(): FilterDsl = custom("ctrlKey")

    infix fun and(block: FilterDsl.() -> Unit): FilterDsl {
        filters.append("&&")
        block()
        return this
    }

    infix fun or(block: FilterDsl.() -> Unit): FilterDsl {
        filters.append("||")
        block()
        return this
    }

    fun shiftKey(): FilterDsl = custom("shiftKey")

    fun custom(key: String): FilterDsl {
        filters.append(key)
        return this
    }

    internal fun isEmpty(): Boolean = filters.isBlank()
    internal fun isNotEmpty(): Boolean = filters.isNotBlank()
    override fun toString(): String = filters.toString()
}

sealed class Modifier(val value: String) {
    override fun toString(): String = value
    class Once internal constructor() : Modifier("once")
    class Changed internal constructor() : Modifier("changed")
    class Delay internal constructor(private val timing: String) : Modifier("delay") {
        override fun toString(): String {
            return "$value:$timing"
        }
    }

    class Throttle internal constructor(private val timing: String) : Modifier("throttle") {
        override fun toString(): String {
            return "$value:$timing"
        }
    }

    class Target internal constructor(private val cssSelector: String) : Modifier("target") {
        override fun toString(): String {
            return "$value:$cssSelector"
        }
    }

    class Consume internal constructor() : Modifier("consume")

    class Queue internal constructor(private val queueOption: QueueOption.Option) : Modifier("queue") {
        override fun toString(): String {
            return "$value:${queueOption.value}"
        }
    }
}

val Int.sec
    get() = "${this}s"

object QueueOption {
    enum class Option(val value: String) {
        FIRST("first"),
        LAST("last"),
        ALL("all"),
        NONE("none")
    }

    val first = Option.FIRST
    val last = Option.LAST
    val all = Option.ALL
    val none = Option.NONE
}

data class EventDsl internal constructor(
    val eventName: String,
    val filter: FilterDsl = FilterDsl(),
) {
    fun filter(block: FilterDsl.() -> Unit): FilterDsl {
        return filter.apply(block)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other !is EventDsl) return false
        return eventName == other.eventName
    }

    override fun hashCode(): Int {
        return eventName.hashCode()
    }

    override fun toString(): String {
        return buildString {
            append(eventName)
            if (filter.isNotEmpty()) {
                append('[')
                append(filter.toString())
                append(']')
            }
        }
    }
}

data class PollingDsl internal constructor(internal var timing: String = "", internal var condition: String = "") {
    internal fun isEmpty(): Boolean = timing.isBlank()
    internal fun isNotEmpty(): Boolean = timing.isNotBlank()

    infix fun condition(condition: String): PollingDsl {
        this.condition = condition
        return this
    }

    override fun toString(): String = buildString {
        append("every")
        append(" $timing")
        if(condition.isNotBlank()) {
            append(" [$condition]")
        }
    }
}

data class TriggerDsl internal constructor(
    internal val events: MutableSet<EventDsl> = mutableSetOf(),
    internal val polling: PollingDsl = PollingDsl(),
    internal val modifiers: MutableList<Modifier> = mutableListOf()
) {
    // Mouse Events
    fun click(block: EventDsl.() -> Unit) = addEvent("click", block)
    fun dblclick(block: EventDsl.() -> Unit) = addEvent("dblclick", block)
    fun drag(block: EventDsl.() -> Unit) = addEvent("drag", block)
    fun dragend(block: EventDsl.() -> Unit) = addEvent("dragend", block)
    fun dragenter(block: EventDsl.() -> Unit) = addEvent("dragenter", block)
    fun dragleave(block: EventDsl.() -> Unit) = addEvent("dragleave", block)
    fun dragover(block: EventDsl.() -> Unit) = addEvent("dragover", block)
    fun dragstart(block: EventDsl.() -> Unit) = addEvent("dragstart", block)
    fun drop(block: EventDsl.() -> Unit) = addEvent("drop", block)
    fun mousedown(block: EventDsl.() -> Unit) = addEvent("mousedown", block)
    fun mousemove(block: EventDsl.() -> Unit) = addEvent("mousemove", block)
    fun mouseout(block: EventDsl.() -> Unit) = addEvent("mouseout", block)
    fun mouseover(block: EventDsl.() -> Unit) = addEvent("mouseover", block)
    fun mouseup(block: EventDsl.() -> Unit) = addEvent("mouseup", block)
    fun mousewheel(block: EventDsl.() -> Unit) = addEvent("mousewheel", block)
    fun onscroll(block: EventDsl.() -> Unit) = addEvent("onscroll", block)

    // Keyboard Events
    fun keydown(block: EventDsl.() -> Unit) = addEvent("keydown", block)
    fun keyup(block: EventDsl.() -> Unit) = addEvent("keyup", block)
    fun keypress(block: EventDsl.() -> Unit) = addEvent("keypress", block)

    fun blur(block: EventDsl.() -> Unit) = addEvent("blur", block)
    fun change(block: EventDsl.() -> Unit) = addEvent("change", block)
    fun focus(block: EventDsl.() -> Unit) = addEvent("focus", block)
    fun select(block: EventDsl.() -> Unit) = addEvent("select", block)
    fun submit(block: EventDsl.() -> Unit) = addEvent("submit", block)
    fun input(block: EventDsl.() -> Unit) = addEvent("input", block)
    fun reset(block: EventDsl.() -> Unit) = addEvent("reset", block)

    fun error(block: EventDsl.() -> Unit) = addEvent("error", block)
    fun unload(block: EventDsl.() -> Unit) = addEvent("unload", block)
    fun message(block: EventDsl.() -> Unit) = addEvent("message", block)
    fun offline(block: EventDsl.() -> Unit) = addEvent("offline", block)
    fun pagehide(block: EventDsl.() -> Unit) = addEvent("pagehide", block)
    fun pageshow(block: EventDsl.() -> Unit) = addEvent("pageshow", block)

    fun load(block: EventDsl.() -> Unit) = addEvent("load", block)
    fun revealed(block: EventDsl.() -> Unit) = addEvent("revealed", block)

    // Polling
    fun every(block: PollingDsl.() -> String): PollingDsl {
        polling.apply { this.timing = block() }
        return polling
    }

    // Modifiers
    fun once() = addModifier(Modifier.Once())
    fun changed() = addModifier(Modifier.Changed())
    fun delay(timing: String) = addModifier(Modifier.Delay(timing))
    fun throttle(timing: String) = addModifier(Modifier.Throttle(timing))
    fun target(cssSelector: String) = addModifier(Modifier.Target(cssSelector))
    fun consume() = addModifier(Modifier.Consume())
    fun queue(queueOption: QueueOption.Option) = addModifier(Modifier.Queue(queueOption))

    private fun addModifier(modifier: Modifier): TriggerDsl {
        this.modifiers.add(modifier)
        return this
    }

    private inline fun addEvent(eventName: String, block: EventDsl.() -> Unit): TriggerDsl {
        val event = EventDsl(eventName).apply(block)
        events.add(event)
        return this
    }

    private fun valid(): Boolean {
        return !(polling.isNotEmpty() && events.isNotEmpty())
    }

    override fun toString(): String {
        val result = buildString {
            if (events.isNotEmpty()) {
                events.forEachIndexed { index, eventDsl ->
                    append(eventDsl.toString())
                    if (index < events.size - 1) {
                        append(", ")
                    }
                }
                for (modifier in modifiers) {
                    append(" ")
                    append(modifier.toString())
                }
            }

            if(polling.isNotEmpty()) {
                append(polling.toString())
            }
        }

        if (!valid()) {
            throw InvalidTriggerException("Invalid trigger:\n $result")
        }

        return result
    }
}
