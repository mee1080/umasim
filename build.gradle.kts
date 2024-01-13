plugins {
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.kotlin.js) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
}

buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    dependencies {
//        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.31")
//        classpath("org.jetbrains.kotlin:kotlin-serialization:1.5.31")
        classpath("net.meilcli.librarian:plugin-core:1.0.1")
        classpath("net.meilcli.librarian:plugin-preset:1.0.1")
    }
}

allprojects {
    repositories {
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers/")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/wasm/experimental")
    }
}