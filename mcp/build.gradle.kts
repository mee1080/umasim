plugins {
    alias(libs.plugins.kotlin.jvm)
}

group = "io.github.mee1080.umasim"
version = "1.0"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":utility"))
    implementation(project(":race"))
    implementation(libs.kotlinx.coroutinesCore)
    implementation(libs.ktor.serverCio)
    implementation(libs.mcp)
    implementation(libs.slf4j.simple)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(libs.versions.jvmTarget.get().toInt())
}