plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
}

group = "io.github.mee1080.umasim"
version = "1.0"

repositories {
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

composeCompiler {
    enableStrongSkippingMode = true
}

kotlin {
    js(IR) {
        browser()
        binaries.executable()
    }
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(project(":utility"))
                implementation(project(":core"))
                implementation(compose.html.core)
                implementation(compose.runtime)
                implementation(npm("@material/web", libs.versions.materialWeb.get()))
                implementation(libs.kotlinx.serializationJson)
                implementation(libs.kotlinx.coroutinesCore)
            }
        }
    }
}
