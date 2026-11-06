plugins {
    alias(libs.plugins.buildkonfig) apply false
    alias(libs.plugins.compose) apply false
    alias(libs.plugins.kotlin.js) apply false
    alias(libs.plugins.kotlin.jvm) apply false
    alias(libs.plugins.kotlin.kapt) apply false
    alias(libs.plugins.kotlin.multiplatform) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
}

buildscript {
    repositories {
        gradlePluginPortal()
        mavenCentral()
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

// JDK 19以降、パイプ出力時の System.out は stdout.encoding（Windowsでは MS932）になり、
// UTF-8 で読む Gradle / IntelliJ の実行ペインで文字化けするため UTF-8 に固定する
subprojects {
    tasks.withType<JavaExec>().configureEach {
        jvmArgs("-Dstdout.encoding=UTF-8", "-Dstderr.encoding=UTF-8")
    }
}