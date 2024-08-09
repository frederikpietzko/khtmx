package de.fpietzko.khtmx.core

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class FilterDslTest : ShouldSpec({
    should("build correct and filter") {
        val filter = with(FilterDsl()) {
            ctrlKey() and { shiftKey() }
            filters.toString()
        }
        filter shouldBe "ctrlKey&&shiftKey"
    }

    should("build correct or filter") {
        val filter = with(FilterDsl()) {
            ctrlKey() or { shiftKey() }
            filters.toString()
        }
        filter shouldBe "ctrlKey||shiftKey"
    }
})