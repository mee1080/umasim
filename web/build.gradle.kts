plugins {
    kotlin("multiplatform")
    id("org.jetbrains.compose")
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
                implementation(npm("@material/mwc-button", "0.25.3"))
                implementation(npm("@material/mwc-select", "0.25.3"))
                implementation(npm("@material/mwc-list", "0.25.3"))
                implementation(npm("@material/mwc-textfield", "0.25.3"))
                implementation(npm("@material/mwc-formfield", "0.25.3"))
                implementation(npm("@material/mwc-radio", "0.25.3"))
                implementation(npm("@material/mwc-checkbox", "0.25.3"))
                implementation(npm("@material/mwc-tab", "0.25.3"))
                implementation(npm("@material/mwc-tab-bar", "0.25.3"))
                implementation(npm("@material/mwc-slider", "0.25.3"))
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