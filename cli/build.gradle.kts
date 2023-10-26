plugins {
    kotlin("jvm")
}

version = "unspecified"

repositories {
    mavenCentral()
}

dependencies {
    implementation(project(":core"))
    implementation(project(":jvm"))
    implementation(kotlin("stdlib"))
    implementation("com.github.ajalt.clikt:clikt:3.4.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
}

val jar by tasks.getting(Jar::class) {
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    manifest {
        attributes["Main-Class"] = "io.github.mee1080.umasim.cli.CliMainKt"
    }
    configurations["runtimeClasspath"].forEach { file: File ->
        from(zipTree(file.absoluteFile))
    }
    exclude("META-INF/*.RSA", "META-INF/*.SF", "META-INF/*.DSA")
}