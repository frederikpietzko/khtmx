package io.github.frederikpietzko.core

import kotlinx.html.CommonAttributeGroupFacade
import kotlinx.html.classes
import kotlinx.html.head
import kotlinx.html.html
import kotlinx.html.stream.appendHTML

var CommonAttributeGroupFacade.hxBoost: Boolean
    get() = this.attributes["hx-boost"]?.toBoolean() ?: false
    set(value) {
        this.attributes["hx-boost"] = value.toString()
    }

fun CommonAttributeGroupFacade.hxBoost(value: Boolean) {
    this.hxBoost = value
}

var CommonAttributeGroupFacade.hxTrigger: String
    get() = this.attributes["hx-trigger"] ?: ""
    set(value) {
        this.attributes["hx-trigger"] = value
    }

fun CommonAttributeGroupFacade.hxTrigger(value: String) {
    this.hxTrigger = value
}

fun CommonAttributeGroupFacade.hxTrigger(block: TriggerDsl.() -> Unit) =
    TriggerDsl()
        .block()
        .toString()
        .also {
            hxTrigger = it
        }

var CommonAttributeGroupFacade.hxTarget: String
    get() = this.attributes["hx-target"] ?: ""
    set(value) {
        this.attributes["hx-target"] = value
    }

fun CommonAttributeGroupFacade.hxTarget(value: String) {
    this.hxTarget = value
}

var CommonAttributeGroupFacade.hxGet: String
    get() = this.attributes["hx-get"] ?: ""
    set(value) {
        this.attributes["hx-get"] = value
    }

fun CommonAttributeGroupFacade.hxGet(value: String) {
    this.hxGet = value
}

var CommonAttributeGroupFacade.hxPost: String
    get() = this.attributes["hx-post"] ?: ""
    set(value) {
        this.attributes["hx-post"] = value
    }

fun CommonAttributeGroupFacade.hxPost(value: String) {
    this.hxPost = value
}

var CommonAttributeGroupFacade.hxPut: String
    get() = this.attributes["hx-put"] ?: ""
    set(value) {
        this.attributes["hx-put"] = value
    }

fun CommonAttributeGroupFacade.hxPut(value: String) {
    this.hxPut = value
}

var CommonAttributeGroupFacade.hxPatch: String
    get() = this.attributes["hx-patch"] ?: ""
    set(value) {
        this.attributes["hx-patch"] = value
    }

fun CommonAttributeGroupFacade.hxPatch(value: String) {
    this.hxPatch = value
}

var CommonAttributeGroupFacade.hxDelete: String
    get() = this.attributes["hx-delete"] ?: ""
    set(value) {
        this.attributes["hx-delete"] = value
    }

fun CommonAttributeGroupFacade.hxDelete(value: String) {
    this.hxDelete = value
}

fun CommonAttributeGroupFacade.hxIndicator() {
    this.classes += setOf("htmx-indicator")
}


var CommonAttributeGroupFacade.hxSwap: String?
    get() = this.attributes["hx-swap"]
    set(value) {
        this.attributes["hx-swap"] = value ?: ""
    }

val Int.sec
    get() = "${this}s"

fun CommonAttributeGroupFacade.hxSwap(block: SwapDsl.() -> Unit) {
    this.hxSwap = SwapDsl().apply(block).toString()
}

var CommonAttributeGroupFacade.hxSwapOob: Boolean
    get() = this.attributes["hx-swap-oob"]?.toBoolean() ?: false
    set(value) {
        this.attributes["hx-swap-oob"] = value.toString()
    }

fun CommonAttributeGroupFacade.hxSwapOob(value: Boolean) {
    this.hxSwapOob = value
}

// TODO: Sync Dsl
var CommonAttributeGroupFacade.hxSync: String
    get() = this.attributes["hx-sync"] ?: ""
    set(value) {
        this.attributes["hx-sync"] = value
    }

fun CommonAttributeGroupFacade.hxSync(value: String) {
    this.hxSync = value
}

var CommonAttributeGroupFacade.hxConfirm: String
    get() = this.attributes["hx-confirm"] ?: ""
    set(value) {
        this.attributes["hx-confirm"] = value
    }

fun CommonAttributeGroupFacade.hxConfirm(value: String) {
    this.hxConfirm = value
}

var CommonAttributeGroupFacade.hxPushUrl: Boolean
    get() = this.attributes["hx-push-url"]?.toBoolean() ?: false
    set(value) {
        this.attributes["hx-push-url"] = value.toString()
    }

var CommonAttributeGroupFacade.hxDisable: Boolean
    get() = this.attributes["hx-disable"] != null
    set(value) {
        if (value) {
            this.attributes["hx-disable"] = ""
        } else {
            this.attributes.remove("hx-disable")
        }
    }

// TODO: Configuration Dsl
fun test() {
    buildString {
        appendHTML().html {
            head {

            }
        }
    }
}