import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    jvmToolchain(libs.versions.jvmTarget.get().toInt())

    jvm()

    js(IR) {
        browser()
        binaries.executable()
    }

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
        compilerOptions {
            freeCompilerArgs.add("-Xwasm-debugger-custom-formatters")
            freeCompilerArgs.add("-Xwasm-attach-js-exception")
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.multiplatform.settings)
                implementation(libs.kotlinx.serializationJson)
                implementation(libs.kotlinx.coroutinesCore)
            }
        }

        val commonTest by getting {
            dependencies {
                implementation(libs.test.common)
                implementation(libs.test.annotations)
            }
        }

        val jvmMain by getting {
            dependencies {
//                implementation(libs.ktor.clientCio)
            }
        }

        val jvmTest by getting {
            dependencies {
                implementation(libs.test.junit)
            }
        }

        val wasmJsMain by getting {
            dependencies {
                implementation(libs.kotlinx.browser)
            }
        }
    }
}