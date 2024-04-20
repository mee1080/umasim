import org.jetbrains.kotlin.gradle.targets.js.dsl.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    jvm()

    js(IR) {
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
                implementation(libs.kotlinx.collections.immutable)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.test.common)
                implementation(libs.test.annotations)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(libs.test.junit)
            }
        }
    }
}