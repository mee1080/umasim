plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "0.5.0-build245"
}

group = "io.github.mee1080.umasim"
version = "1.0"

repositories {
    maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
}

kotlin {
    js(IR) {
        browser()
        binaries.executable()
    }
    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(project(":core"))
                implementation(compose.web.core)
                implementation(compose.runtime)
            }
        }
    }
}