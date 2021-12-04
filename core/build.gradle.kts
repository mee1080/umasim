plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
    id("librarian")
    id("librarian-preset")
}

group = "io.github.mee1080.umasim"
version = "1.0"

kotlin {

    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "15"
        }
    }

    js("web",IR) {
        useCommonJs()
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
                implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}

librarian {
    failOnGeneratePageWhenFoundPlaceholder = false
    pages {
        create("UmasimCore") {
            title = "Using Libraries"
            description = "Umasim is using these libraries."
            configurations {
                contain {
                    value = mutableListOf(
                        "commonMainImplementationDependenciesMetadata",
                        "desktopDefault",
                        "desktopMainApiDependenciesMetadata",
                        "desktopRuntimeClasspath",
                        "webDefault",
                        "webMainApiDependenciesMetadata",
                        "webRuntimeClasspath",
                    )
                }
            }
        }
    }
}