import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    id("librarian")
    id("librarian-preset")
}

group = "io.github.mee1080.umasim"
version = "1.0"

dependencies {
    implementation(project(":core"))
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
    implementation("io.ktor:ktor-client-cio:1.6.2")
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "15"
}

librarian {
    failOnGeneratePageWhenFoundPlaceholder = false
    ignoreArtifacts = mutableListOf(
        "io.github.mee1080.umasim:core",
        "io.github.mee1080.umasim:jvm",
        "io.github.mee1080.umasim:desktopSupport",
    )
    pages {
        create("UmasimJvm") {
            title = "Using Libraries"
            description = "Umasim is using these libraries."
            configurations {
                contain {
                    value = mutableListOf(
                        "default",
                        "implementationDependenciesMetadata",
                        "runtimeClasspath",
                    )
                }
            }
        }
    }
}