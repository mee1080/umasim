import com.codingfeline.buildkonfig.compiler.FieldSpec.Type.STRING

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.buildkonfig) // This is com.codingfeline.buildkonfig
}

buildkonfig { // Corrected from buildConfig to buildkonfig
    packageName = "io.github.mee1080.umasim.chat"
    defaultConfigs { // buildkonfig typically uses a nested defaultConfigs block
        buildConfigField(STRING, "GEMINI_API_KEY", "\"YOUR_API_KEY_HERE_OR_READ_FROM_ENV\"")
    }
}

group = "io.github.mee1080.umasim"
version = "1.0"

kotlin {
    jvmToolchain(libs.versions.jvmTarget.get().toInt())

    jvm("desktop")
    js("web", IR) {
        browser()
        useCommonJs()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("ai.koog:koog-agents:0.1.0")
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.ui)
                implementation("com.russhwolf:multiplatform-settings-no-arg:1.3.0")
                implementation(project(":compose"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.test)
                implementation(libs.test.common)
                implementation(libs.test.annotations)
            }
        }
    }
}
