import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
    kotlin("plugin.serialization")
}

group = "io.github.mee1080.umasim"
version = "1.0"

dependencies {
    implementation(project(":core"))
    implementation(project(":jvm"))
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.2.2")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "16"
}
