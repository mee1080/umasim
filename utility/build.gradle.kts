import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

kotlin {
    jvmToolchain(libs.versions.jvmTarget.get().toInt())

    jvm()

    js {
        browser()
        binaries.executable()
    }

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
            implementation(libs.multiplatform.settings)
            implementation(libs.kotlinx.serializationJson)
            implementation(libs.kotlinx.coroutinesCore)
        }

        commonTest.dependencies {
            implementation(libs.test.common)
            implementation(libs.test.annotations)
        }

        jvmMain.dependencies {
//                implementation(libs.ktor.clientCio)
        }

        jvmTest.dependencies {
            implementation(libs.test.junit)
        }

        wasmJsMain.dependencies {
            implementation(libs.kotlinx.browser)
        }
    }
}