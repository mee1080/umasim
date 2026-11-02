plugins {
    alias(libs.plugins.kotlin.multiplatform)
}

group = "io.github.mee1080.umasim"
version = "1.0"

kotlin {

    jvmToolchain(libs.versions.jvmTarget.get().toInt())

    jvm()

    sourceSets {
        jvmMain.dependencies {
            implementation(project(":utility"))
            implementation(project(":core"))
            implementation(kotlin("stdlib"))
            implementation(libs.kotlinx.coroutinesCore)
            implementation(libs.ktor.clientCio)
            implementation(libs.sqlite.jdbc)
        }
    }
}

// io.github.mee1080.umasim.cui.main を実行（../data/ を読むため作業ディレクトリはモジュール直下）
tasks.register<JavaExec>("run") {
    group = "application"
    val jvmJar = tasks.named<Jar>("jvmJar")
    dependsOn(jvmJar)
    classpath = files(jvmJar.flatMap { it.archiveFile }) + configurations.getByName("jvmRuntimeClasspath")
    mainClass.set("io.github.mee1080.umasim.cui.MainKt")
    workingDir = projectDir
    javaLauncher.set(javaToolchains.launcherFor {
        languageVersion.set(JavaLanguageVersion.of(libs.versions.jvmTarget.get().toInt()))
    })
}
