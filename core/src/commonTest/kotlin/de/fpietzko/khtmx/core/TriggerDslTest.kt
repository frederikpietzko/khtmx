package de.fpietzko.khtmx.core

import de.fpietzko.khtmx.core.QueueOption.all
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class EventDslTest : ShouldSpec({
    should("build event correctly") {
        val event = with(EventDsl("click")) { toString() }
        event shouldBe "click"
    }

    should("build event with filter") {
        val event = with(EventDsl("click")) {
            filter { ctrlKey() }
            toString()
        }
        event shouldBe "click[ctrlKey]"
    }

    should("build evnet with multiple filters") {
        val event = with(EventDsl("click")) {
            filter { ctrlKey() and { shiftKey() } }
            toString()
        }
        event shouldBe "click[ctrlKey&&shiftKey]"
    }
})

class TriggerDslTest : ShouldSpec({
    should("build event event") {
        val trigger = trigger { click { } }
        trigger shouldBe "click"
    }

    should("build click event with filter") {
        val trigger = trigger { click { filter { ctrlKey() } } }
        trigger shouldBe "click[ctrlKey]"
    }

    should("build click event with multiple filters") {
        val trigger = trigger { click { filter { ctrlKey() and { shiftKey() } } } }
        trigger shouldBe "click[ctrlKey&&shiftKey]"
    }

    should("build click event with modifier") {
        val trigger = trigger {
            click { }
            once()
            changed()
            delay(1.sec)
            throttle(1.sec)
            target(".selector")
            consume()
            queue(all)
        }
        trigger shouldBe "click once changed delay:1s throttle:1s target:.selector consume queue:all"
    }

    should("build click event with modifiers and filters") {
        val trigger = trigger {
            click { filter { ctrlKey() } }
            once()
            delay(1.sec)
        }
        trigger shouldBe "click[ctrlKey] once delay:1s"
    }

    should("build multiple events") {
        val trigger = trigger {
            click { }
            mouseover { }
        }
        trigger shouldBe "click, mouseover"
    }

    should("build multiple events with different filters") {
        val trigger = trigger {
            click { filter { ctrlKey() } }
            mouseover { filter { shiftKey() } }
        }
        trigger shouldBe "click[ctrlKey], mouseover[shiftKey]"
    }

    should("build multiple events with modifier") {
        val trigger = trigger {
            click { }
            mouseover { }
            once()
            delay(1.sec)
        }
        trigger shouldBe "click, mouseover once delay:1s"
    }

    should("build multiple events with filters and modifiers") {
        val trigger = trigger {
            click { filter { ctrlKey() and { shiftKey() } } }
            mouseover { filter { ctrlKey() or { shiftKey() } } }
            once()
            delay(1.sec)
        }

        trigger shouldBe "click[ctrlKey&&shiftKey], mouseover[ctrlKey||shiftKey] once delay:1s"
    }

    should("build polling trigger") {
        val trigger = trigger {
            every { 1.sec }
        }
        trigger shouldBe "every 1s"
    }

    should("build polling trigger with conditional") {
        val trigger = trigger {
            every { 1.sec } condition "someCondition"
        }
        trigger shouldBe "every 1s [someCondition]"
    }

    should("throw InvalidTriggerException") {
        shouldThrow<InvalidTriggerException> {
            trigger {
                click {  }
                every { 1.sec }
            }
        }
    }
})

internal fun trigger(block: TriggerDsl.() -> Unit): String {
    return TriggerDsl().apply(block).toString()
}