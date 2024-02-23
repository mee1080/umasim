pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
        mavenCentral()
    }

}
plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.5.0"
}
rootProject.name = "umasim"
include("jvm")
include("core")
include("core")
include("web")
include("desktop")
include("cli")
include("race")
include("wasm")
include("compose")
include("desktop2")
