pluginManagement {
    repositories {
        gradlePluginPortal()
        maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
    }

}
rootProject.name = "umasim"
include("jvm")
include("core")
include("core")
include("web")
include("desktop")
include("cli")
