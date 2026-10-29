import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.compose)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
}

group = "io.github.mee1080.umasim"
version = "1.0.1"

repositories {
    maven { url = uri("https://maven.pkg.jetbrains.space/public/p/compose/dev") }
}

dependencies {
    implementation(project(":core"))
    implementation(project(":jvm"))
    implementation(compose.desktop.currentOs)
    implementation(libs.compose.materialIconsCore)
    implementation(libs.kotlinx.serializationJson)
    implementation(libs.kotlinx.coroutinesCore)
}

kotlin {
    jvmToolchain(libs.versions.jvmTarget.get().toInt())
}

compose.desktop {
    application {
        mainClass = "io.github.mee1080.umasim.gui.MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "umasim"
            packageVersion = "1.0.0"
        }
    }
}
