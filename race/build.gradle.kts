plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

group = "io.github.mee1080.umasim"
version = "1.0"

kotlin {

    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "16"
        }
    }

    js("web", IR) {
        useCommonJs()
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.2")
            }
        }
    }
}
