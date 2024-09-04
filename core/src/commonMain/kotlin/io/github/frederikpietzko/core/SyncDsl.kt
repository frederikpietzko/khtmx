package io.github.frederikpietzko.core

object QueueModifier {
    enum class Modifier(val value: String) {
        FIRST("first"),
        LAST("last"),
        ALL("all"),
    }

    val first = Modifier.FIRST
    val last = Modifier.LAST
    val all = Modifier.ALL
}

sealed class SyncStrategy(protected val value: String) {
    override fun toString(): String = value
    class Drop internal constructor(): SyncStrategy("drop")
    class Abort internal constructor(): SyncStrategy("abort")
    class Replace internal constructor(): SyncStrategy("replace")
    class Queue internal constructor(private val queueModifier: QueueModifier.Modifier): SyncStrategy("queue") {
        override fun toString(): String {
            return "$value ${queueModifier.value}"
        }
    }
}

object CommonSelectors {
    val `this` = "this"
    val closest = "closest"
    val closestForm = "$closest form"
}

data class SyncDsl internal constructor(
    var selector: String? = null,
    var strategy: SyncStrategy? = null,
) {
    fun validate() {
        requireNotNull(selector)
        requireNotNull(strategy)
    }

    override fun toString(): String {
        return "$selector:$strategy"
    }
}