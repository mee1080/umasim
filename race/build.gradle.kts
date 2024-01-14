import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

group = "io.github.mee1080.umasim"
version = "1.0"

kotlin {

    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = libs.versions.jvmTarget.get()
        }
    }

    js("web", IR) {
        useCommonJs()
        browser()
    }

//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs()

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.kotlinx.coroutinesCore)
                implementation(libs.kotlinx.serializationJson)
            }
        }
    }
}
