package io.github.frederikpietzko.core

import io.kotest.core.spec.style.ShouldSpec
import io.kotest.matchers.shouldBe

class SwapDslTest : ShouldSpec({
    should("build simple swap correctly") {
        val swap = with(SwapDsl()) {
            innerHtml {}
            toString()
        }
        swap shouldBe "innerHTML"
    }

    should("build swap with modifiers correctly") {
        val swap = with(SwapDsl()) {
            innerHtml {
                swap(1.sec)
            }
            toString()
        }
        swap shouldBe "innerHTML swap:1s"
    }

    should("build swap with multiple modifiers") {
        val swap = with(SwapDsl()) {
            innerHtml {
                swap(1.sec)
                scroll {
                    top()
                    target("#testing")
                }
            }
            toString()
        }

        swap shouldBe "innerHTML swap:1s scroll:#testing:top"
    }
})