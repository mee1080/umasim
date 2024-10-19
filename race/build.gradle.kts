import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

group = "io.github.mee1080.umasim"
version = "1.0"

kotlin {

    jvmToolchain(libs.versions.jvmTarget.get().toInt())

    jvm("desktop")

    js("web", IR) {
        useCommonJs()
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":utility"))
                implementation(libs.kotlinx.coroutinesCore)
                implementation(libs.kotlinx.serializationJson)
            }
        }
    }
}
