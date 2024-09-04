import org.jreleaser.model.Active
import org.jreleaser.model.Active.ALWAYS

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotest.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    `maven-publish`
    signing
    alias(libs.plugins.jreleaser)
}

group = "io.github.frederikpietzko"
version = "0.0.1"

kotlin {
    jvm { }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlinx.html.common)
            implementation(libs.kotlinx.serialization.json)
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

val releasesRepoUrl = layout.buildDirectory.dir("repos/releases")
val snapshotsRepoUrl = layout.buildDirectory.dir("repos/snapshots")
val repo = if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl

publishing {
    repositories {
        maven {
            url = uri(repo)
        }
    }
    publications.withType<MavenPublication> {
        pom {
            name = "khtmx-core"
            description = "khtmx-core version $version - Kotlin Bindings that integrate htmx into kotlinx.html"
            url = "https://github.com/frederikpietzko/khtmx"
            licenses {
                license {
                    name = "The Apache Software License, Version 2.0"
                    url = "https://www.apache.org/licenses/LICENSE-2.0.txt"
                }
            }
            developers {
                developer {
                    name = "Frederik Pietzko"
                    email = "pietzkofred@gmail.com"
                }
            }
            scm {
                connection = "scm:git:git://github.com/frederikpietzko/khtmx.git"
                developerConnection = "scm:git:ssh://github.com/frederikpietzko/khtmx.git"
                url = "https://github.com/frederikpietzko/khtmx/tree/main"
            }
        }
    }
}

signing {
    sign(publishing.publications)
}

tasks.withType<AbstractPublishToMaven>().configureEach {
    val signingTasks = tasks.withType<Sign>()
    mustRunAfter(signingTasks)
}

jreleaser {
    signing {
        active = ALWAYS
        armored = true
    }
    deploy {
        maven {
            mavenCentral {
                create("sonatype") {
                    active = ALWAYS
                    url = "https://central.sonatype.com/api/v1/publisher"
                    stagingRepository(repo.get().asFile.path)
                }
            }
        }
    }
}
