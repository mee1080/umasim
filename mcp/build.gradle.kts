plugins {
    kotlin("jvm") version "2.1.20"
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
    implementation(libs.mcp)
    implementation(libs.slf4j.simple)
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(17)
}