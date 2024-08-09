rootProject.name = "khtmx"

pluginManagement {
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

val kotlinx_html_version = "0.11.0"
val kotest_version = "5.0.2"

dependencyResolutionManagement {
    repositories {
        mavenCentral()
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}


include("core")
