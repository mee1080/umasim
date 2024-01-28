plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
}

group = "io.github.mee1080.umasim"
version = "1.0"

kotlin {

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = libs.versions.jvmTarget.get()
        }
    }

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":compose"))
                implementation(compose.desktop.currentOs)
                implementation(libs.kotlinx.coroutinesCore)
            }
        }
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
    }
}