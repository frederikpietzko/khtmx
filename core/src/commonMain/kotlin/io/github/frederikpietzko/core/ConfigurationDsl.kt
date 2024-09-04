package io.github.frederikpietzko.core

import kotlinx.html.HEAD
import kotlinx.html.meta
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
data class HTMXConfiguration(
    var historyEnabled: Boolean = true,
    var historyCacheSize: Int = 10,
    var refreshOnHistoryMiss: Boolean = false,
    var defaultSwapStyle: SwapType = SwapType.INNER_HTML,
    var defaultSwapDelay: Int = 0,
    var defaultSettleDelay: Int = 20,
    var includeIndicatorStyles: Boolean = true,
    var indicatorClass: String = "htmx-indicator",
    var requestClass: String = "htmx-request",
    var addedClass: String = "htmx-added",
    var settlingClass: String = "htmx-settling",
    var swappingClass: String = "htmx-swapping",
    var allowEval: Boolean = true,
    var allowScriptTags: Boolean = true,
    var inlineScriptNonce: String = "",
    var attributesToSettle: List<String> = listOf("class", "style", "width", "height"),
    var inlineStyleNonce: String = "",
    var updateTemplateFragments: Boolean = false,
    var wsReconnectDelay: String = "full-jitter",
    var wsBinaryType: String = "blob",
    var disableSelector: String = "[hx-disable], [data-hx-disable]",
    var withCredentials: Boolean = false,
    var timeout: Int = 0,
    var scrollBehaviour: String = "smooth",
    var defaultFocusScroll: Boolean = false,
    var getCacheBusterParams: Boolean = false,
    var globalViewTransitions: Boolean = false,
    var methodsThatUseUrlParams: List<String> = listOf("get"),
    var selfRequestsOnly: Boolean = true,
    var ignoreTitle: Boolean = false,
    var disableInheritance: Boolean = false,
    var scrollIntoViewOnBoost: Boolean = true,
    var triggerSpecsCache: String? = null,
    var allowNestedOobSwaps: Boolean = true,
)

fun HEAD.htmxConfig(block: HTMXConfiguration.() -> Unit) {
    val config = HTMXConfiguration().apply(block)
    meta {
        name = "htmx-config"
        content = Json.encodeToString(config)
    }
}