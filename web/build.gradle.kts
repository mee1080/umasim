plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
    id("librarian")
    id("librarian-preset")
    kotlin("plugin.serialization")
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
                implementation(compose.html.core)
                implementation(compose.runtime)
                implementation(npm("@material/web", "1.0.0"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
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