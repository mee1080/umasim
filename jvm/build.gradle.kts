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
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("io.ktor:ktor-client-cio:2.2.4")
    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "16"
    kotlinOptions.freeCompilerArgs = listOf("-Xcontext-receivers")
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