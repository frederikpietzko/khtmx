plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotest.multiplatform)
}

group = "de.fpietzko.khtmx"
version = "0.0.1"

kotlin {
    jvm { }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.html.common)
        }

        commonTest.dependencies {
            implementation(kotlin("test"))
            implementation(libs.kotest.assertions.core)
            implementation(libs.kotest.framework.engine)
            implementation(libs.kotest.framework.datatest)
        }

        jvmMain.dependencies {
            implementation(libs.kotlinx.html.jvm)
        }

        jvmTest.dependencies {
            implementation(libs.kotest.runner.junit5)
        }
    }
}

tasks.withType<Test>().configureEach {
    useJUnitPlatform()
}