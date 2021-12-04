buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.5.21")
        classpath("org.jetbrains.kotlin:kotlin-serialization:1.5.21")
        classpath("net.meilcli.librarian:plugin-core:1.0.1")
        classpath("net.meilcli.librarian:plugin-preset:1.0.1")
    }
}

allprojects {
    repositories {
        gradlePluginPortal()
        mavenCentral()
        google()
    }
}