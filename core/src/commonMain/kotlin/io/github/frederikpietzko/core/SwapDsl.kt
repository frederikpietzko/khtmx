package io.github.frederikpietzko.core

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder


object ScrollAndShowBehaviour {
    enum class Behaviour(val value: String) {
        TOP("top"),
        BOTTOM("bottom"),
        NONE("none")
    }

    val top = Behaviour.TOP
    val bottom = Behaviour.BOTTOM
    val none = Behaviour.NONE
}

data class ScrollAndShowDsl(
    internal var behaviour: ScrollAndShowBehaviour.Behaviour? = null,
    internal var target: String? = null
) {
    fun top() = this.apply { behaviour = ScrollAndShowBehaviour.Behaviour.TOP }
    fun bottom() = this.apply { behaviour = ScrollAndShowBehaviour.Behaviour.BOTTOM }
    fun none() = this.apply { behaviour = ScrollAndShowBehaviour.Behaviour.NONE }
    fun target(target: String) = this.apply { this.target = target }

    internal fun validate(): ScrollAndShowDsl {
        require(behaviour != null) { "Scroll or Show Behaviour cannot be null!" }
        return this
    }
}

sealed class SwapModifier(protected val value: String) {
    override fun toString() = value
    class Transition internal constructor() : SwapModifier("transition")
    class Swap internal constructor(private val timing: String) : SwapModifier("swap") {
        override fun toString(): String {
            return "$value:$timing"
        }
    }

    class Settle internal constructor(private val timing: String) : SwapModifier("settle") {
        override fun toString(): String {
            return "$value:$timing"
        }
    }

    class IgnoreTitle internal constructor() : SwapModifier("ignoreTitle")
    class Scroll internal constructor(
        private val behaviour: ScrollAndShowBehaviour.Behaviour,
        private val target: String? = null
    ) : SwapModifier("scroll") {
        override fun toString(): String = buildString {
            append(value)
            if (target != null) {
                append(":$target")
            }
            append(":${behaviour.value}")
        }
    }

    class Show internal constructor(
        private val behaviour: ScrollAndShowBehaviour.Behaviour,
        private val target: String? = null
    ) : SwapModifier("show") {
        override fun toString(): String = buildString {
            append(value)
            if (target != null) {
                append(":$target")
            }
            append(":${behaviour.value}")
        }
    }

    class FocusScroll(private val focus: Boolean) : SwapModifier("focus-scroll") {
        override fun toString(): String {
            return "$value:$focus"
        }
    }
}

data class ModifierDsl(
    internal val modifiers: MutableList<SwapModifier> = mutableListOf(),
) {

    fun transition(): ModifierDsl {
        modifiers.add(SwapModifier.Transition())
        return this
    }

    fun swap(timing: String): ModifierDsl {
        modifiers.add(SwapModifier.Swap(timing))
        return this
    }

    fun settle(timing: String): ModifierDsl {
        modifiers.add(SwapModifier.Settle(timing))
        return this
    }

    fun ignoreTitle(): ModifierDsl {
        modifiers.add(SwapModifier.IgnoreTitle())
        return this
    }

    fun scroll(block: ScrollAndShowDsl.() -> Unit): ModifierDsl {
        modifiers.add(
            ScrollAndShowDsl()
                .apply(block)
                .validate()
                .let {
                    SwapModifier.Scroll(it.behaviour!!, it.target)
                }
        )
        return this
    }

    fun show(block: ScrollAndShowDsl.() -> Unit): ModifierDsl {
        modifiers.add(
            ScrollAndShowDsl()
                .apply(block)
                .validate()
                .let {
                    SwapModifier.Show(it.behaviour!!, it.target)
                }
        )
        return this
    }

    fun focusScroll(focus: Boolean): ModifierDsl {
        modifiers.add(
            SwapModifier.FocusScroll(focus)
        )
        return this
    }
}

data class SwapDsl internal constructor(
    internal var swapType: SwapType? = null,
    internal val modifierDsl: ModifierDsl = ModifierDsl()
) {
    fun innerHtml(block: ModifierDsl.() -> Unit) =
        setSwapType(SwapType.INNER_HTML, block)

    fun outerHtml(block: ModifierDsl.() -> Unit) =
        setSwapType(SwapType.OUTER_HTML, block)

    fun beforeBegin(block: ModifierDsl.() -> Unit) =
        setSwapType(SwapType.BEFORE_BEGIN, block)

    fun afterBegin(block: ModifierDsl.() -> Unit) =
        setSwapType(SwapType.AFTER_BEGIN, block)

    fun beforeEnd(block: ModifierDsl.() -> Unit) =
        setSwapType(SwapType.BEFORE_END, block)

    fun afterEnd(block: ModifierDsl.() -> Unit) =
        setSwapType(SwapType.AFTER_END, block)

    fun delete(block: ModifierDsl.() -> Unit) =
        setSwapType(SwapType.DELETE, block)

    fun none(block: ModifierDsl.() -> Unit) =
        setSwapType(SwapType.NONE, block)

    override fun toString(): String = buildString {
        append(requireNotNull(swapType).value)
        modifierDsl.modifiers.forEach {
            append(" $it")
        }
    }


    private fun setSwapType(swapType: SwapType, block: ModifierDsl.() -> Unit) =
        this
            .apply { this.swapType = swapType }
            .apply { this.modifierDsl.apply(block) }
}

@Serializable(with = SwapTypeSerializer::class)
enum class SwapType(val value: String) {
    INNER_HTML("innerHTML"),
    OUTER_HTML("outerHTML"),
    BEFORE_BEGIN("beforebegin"),
    AFTER_BEGIN("afterbegin"),
    BEFORE_END("beforeend"),
    AFTER_END("afterend"),
    DELETE("delete"),
    NONE("none");

    companion object {
        fun of(value: String): SwapType {
            return entries.single { it.value == value }
        }
    }
}

object SwapTypeSerializer : KSerializer<SwapType> {
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("SwapType", PrimitiveKind.STRING)

    override fun deserialize(decoder: Decoder): SwapType {
        val string = decoder.decodeString()
        return SwapType.of(string)
    }

    override fun serialize(encoder: Encoder, value: SwapType) {
        val string = value.value
        encoder.encodeString(string)
    }
}