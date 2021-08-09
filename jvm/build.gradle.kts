import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm")
}

group = "io.github.mee1080.umasim"
version = "1.0"

dependencies {
    implementation(project(":core"))
    implementation(kotlin("stdlib"))
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1")
}

tasks.withType<KotlinCompile>() {
    kotlinOptions.jvmTarget = "15"
}
