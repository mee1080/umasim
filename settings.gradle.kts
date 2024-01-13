pluginManagement {
    repositories {
        google()
        gradlePluginPortal()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
        mavenCentral()
    }

}
rootProject.name = "umasim"
include("jvm")
include("core")
include("core")
include("web")
include("desktop")
include("cli")
include("race")
