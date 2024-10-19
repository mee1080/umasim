import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

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
                implementation(libs.multiplatform.settings)
                implementation(libs.kotlinx.serializationJson)
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