plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.0.0-alpha1"
    id("librarian")
    id("librarian-preset")
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

librarian {
    failOnGeneratePageWhenFoundPlaceholder = false
    ignoreArtifacts = mutableListOf(
        "io.github.mee1080.umasim:core",
    )
    pages {
        create("UmasimWeb") {
            title = "Using Libraries"
            description = "Umasim is using these libraries."
            configurations {
                contain {
                    value = mutableListOf(
                        "commonMainApiDependenciesMetadata",
                        "jsDefault",
                        "jsMainImplementationDependenciesMetadata",
                        "jsRuntimeClasspath",
                    )
                }
            }
        }
    }
}