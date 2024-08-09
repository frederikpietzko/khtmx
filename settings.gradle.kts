rootProject.name = "khtmx"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

val kotlinx_html_version = "0.11.0"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
    versionCatalogs {
        create("kotlinxhtml") {
            library("common", "org.jetbrains.kotlinx:kotlinx-html:$kotlinx_html_version")
            library("jvm", "org.jetbrains.kotlinx:kotlinx-html-jvm:$kotlinx_html_version")
            library("js", "org.jetbrains.kotlinx:kotlinx-html-js:$kotlinx_html_version")
            library("wasmJs", "org.jetbrains.kotlinx:kotlinx-html-wasmJs:$kotlinx_html_version")
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}


include("core")
