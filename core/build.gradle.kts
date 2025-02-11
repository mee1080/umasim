plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

group = "io.github.mee1080.umasim"
version = "1.0"

kotlin {

    jvmToolchain(libs.versions.jvmTarget.get().toInt())

    jvm("desktop")

    js("web", IR) {
        useCommonJs()
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(project(":utility"))
                implementation(libs.kotlinx.serializationJson)
                implementation(libs.kotlinx.coroutinesCore)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.test)
                implementation(libs.test.common)
                implementation(libs.test.annotations)
                implementation(libs.kotlinx.coroutinesTest)
            }
        }
    }
}
