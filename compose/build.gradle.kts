import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
}

group = "io.github.mee1080.umasim"
version = "1.0"

composeCompiler {
    enableStrongSkippingMode = true
}

kotlin {

    jvmToolchain(libs.versions.jvmTarget.get().toInt())

    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        moduleName = "wasm"
        browser {
            commonWebpackConfig {
                outputFileName = "wasm.js"
            }
        }
        binaries.executable()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":utility"))
                implementation(project(":race"))
                implementation(compose.runtime)
                implementation(compose.material3)
                implementation(compose.materialIconsExtended)
                @OptIn(org.jetbrains.compose.ExperimentalComposeLibrary::class)
                implementation(compose.components.resources)
                implementation(libs.kotlinx.serializationJson)
                implementation(libs.kotlinx.collections.immutable)
                implementation(libs.kotlinx.coroutinesCore)
                implementation(libs.multiplatform.settings)
                implementation(libs.koalaplot.core)
            }
        }

        val jvmMain by getting {
            dependencies {
                implementation(compose.desktop.currentOs)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}
