plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

group = "io.github.mee1080.umasim"
version = "1.0"

kotlin {

    js(IR) {
        useCommonJs()
        browser()
    }

    sourceSets {
        val jsMain by getting {
            dependencies {
                implementation(project(":core"))
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
            }
        }
    }
}