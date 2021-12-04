import org.jetbrains.compose.compose
import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("org.jetbrains.compose") version "1.0.0-alpha3"
    id("librarian")
    id("librarian-preset")
}

group = "io.github.mee1080.umasim"
version = "1.0.1"

repositories {
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":jvm"))
    implementation(project(":desktopSupport"))
    implementation(compose.desktop.currentOs)
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "15"
}

compose.desktop {
    application {
        mainClass = "io.github.mee1080.umasim.gui.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "umasim"
            packageVersion = "1.0.0"
        }
    }
}

librarian {
    failOnGeneratePageWhenFoundPlaceholder = false
    ignoreArtifacts = mutableListOf(
        "io.github.mee1080.umasim:core",
        "io.github.mee1080.umasim:jvm",
        "io.github.mee1080.umasim:desktopSupport",
    )
    pages {
        create("UmasimDesktop") {
            title = "Using Libraries"
            description = "Umasim is using these libraries."
            configurations {
                contain {
                    value = mutableListOf(
                        "apiDependenciesMetadata",
                        "default",
                        "implementationDependenciesMetadata",
                        "runtimeClasspath",
                    )
                }
            }
        }
    }
}