plugins {
    kotlin("multiplatform") version "1.9.0" apply false
    kotlin("jvm") version "1.9.0" apply false
    kotlin("js") version "1.9.0" apply false
    kotlin("kapt") version "1.9.0" apply false
    kotlin("plugin.serialization") version "1.9.0" apply false
    id("org.jetbrains.compose") version "1.4.3" apply false
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
        gradlePluginPortal()
        mavenCentral()
        google()
        maven("https://maven.pkg.jetbrains.space/public/p/compose/dev")
        maven("https://maven.pkg.jetbrains.space/kotlin/p/kotlin/kotlin-js-wrappers/")
    }
}