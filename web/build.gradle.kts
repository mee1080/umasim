plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose") version "1.0.0"
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
                implementation(project(":webSupport"))
                implementation(compose.web.core)
                implementation(compose.runtime)
            }
        }
    }
}

afterEvaluate {
    rootProject.extensions.configure<org.jetbrains.kotlin.gradle.targets.js.nodejs.NodeJsRootExtension> {
        versions.webpackCli.version = "4.9.0"
    }
}

librarian {
    failOnGeneratePageWhenFoundPlaceholder = false
    ignoreArtifacts = mutableListOf(
        "io.github.mee1080.umasim:core",
        "io.github.mee1080.umasim:webSupport",
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