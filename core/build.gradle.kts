plugins {
    kotlin("multiplatform") version "2.0.0"
}

group = "de.fpietzko.khtmx"
version = "0.0.1"

kotlin {
    jvmToolchain(21)
    jvm { }

    sourceSets {
        commonMain.dependencies {
            implementation(kotlinxhtml.common)
        }

        jvmMain.dependencies {
            implementation(kotlinxhtml.jvm)
        }
    }
}