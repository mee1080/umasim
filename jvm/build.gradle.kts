plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

group = "io.github.mee1080.umasim"
version = "1.0"

kotlin {

    jvmToolchain(libs.versions.jvmTarget.get().toInt())

    jvm("desktop")

    sourceSets {
        val desktopMain by getting {
            dependencies {
                implementation(project(":utility"))
                implementation(project(":core"))
                implementation(kotlin("stdlib"))
                implementation(libs.kotlinx.coroutinesCore)
                implementation(libs.ktor.clientCio)
                implementation(libs.sqlite.jdbc)
            }
        }
    }
}
