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

kotlin {
    js {
        browser()
        binaries.executable()
    }
    sourceSets {
        jsMain.dependencies {
            implementation(project(":utility"))
            implementation(project(":core"))
            implementation(libs.compose.html.core)
            implementation(libs.compose.runtime)
            implementation(npm("@material/web", libs.versions.materialWeb.get()))
            implementation(libs.kotlinx.serializationJson)
            implementation(libs.kotlinx.coroutinesCore)
            implementation(libs.ktor.clientJs)
        }
    }
}
