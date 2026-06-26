import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

group = "io.github.mee1080.umasim"
version = "1.0"

kotlin {

    jvmToolchain(libs.versions.jvmTarget.get().toInt())

    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
        compilerOptions {
            freeCompilerArgs.add("-Xwasm-debugger-custom-formatters")
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":utility"))
            implementation(libs.kotlinx.coroutinesCore)
            implementation(libs.kotlinx.serializationJson)
        }

        jvmMain.dependencies {
            implementation(libs.slf4j.nop)
        }

        wasmJsMain.dependencies {
            implementation(libs.kotlinx.browser)
        }
    }
}
