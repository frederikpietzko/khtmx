package io.github.frederikpietzko.core

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