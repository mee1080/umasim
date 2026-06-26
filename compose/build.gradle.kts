import com.codingfeline.buildkonfig.compiler.FieldSpec
import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.buildkonfig)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
}

group = "io.github.mee1080.umasim"
version = "1.0"

kotlin {

    jvmToolchain(libs.versions.jvmTarget.get().toInt())

    jvm()

    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        outputModuleName = "wasm"
        browser {
            commonWebpackConfig {
                outputFileName = "wasm.js"
            }
        }
        binaries.executable()
        compilerOptions {
            freeCompilerArgs.add("-Xwasm-debugger-custom-formatters")
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":utility"))
            implementation(project(":race"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.material3)
            implementation(libs.compose.materialIconsExtended)
            implementation(libs.compose.components.resources)
            implementation(libs.kotlinx.serializationJson)
            implementation(libs.kotlinx.coroutinesCore)
            implementation(libs.multiplatform.settings)
            implementation(libs.koalaplot.core)
        }

        jvmMain.dependencies {
            implementation(project(":mcp"))
            implementation(compose.desktop.currentOs)

        }

        wasmJsMain.dependencies {
            implementation(libs.kotlinx.browser)
        }
    }
}

compose.desktop {
    application {
        mainClass = "io.github.mee1080.umasim.compose.MainKt"
        nativeDistributions {
            packageName = "umasim"
            packageVersion = "2.0.0"
        }
    }
}

buildkonfig {
    packageName = "io.github.mee1080.umasim"
    defaultConfigs {
        buildConfigField(FieldSpec.Type.STRING, "APP_VERSION", System.getenv("APP_VERSION") ?: "")
    }
}
