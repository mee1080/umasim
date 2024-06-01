import org.jetbrains.kotlin.gradle.dsl.JvmTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "io.github.mee1080.umasim"
version = "1.0"

dependencies {
    implementation(project(":core"))
    implementation(kotlin("stdlib"))
    implementation(libs.kotlinx.coroutinesCore)
    implementation(libs.ktor.clientCio)
    implementation(libs.sqlite.jdbc)
}

tasks.withType<KotlinCompile> {
    compilerOptions {
        jvmTarget.set(JvmTarget.fromTarget(libs.versions.jvmTarget.get()))
        freeCompilerArgs.add("-Xcontext-receivers")
    }
}
