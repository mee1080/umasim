plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose)
    alias(libs.plugins.compose.compiler) // Added compose compiler plugin
    id("com.github.gmazzo.buildconfig") version "5.6.5"
}

buildKonfig {
    packageName = "io.github.mee1080.umasim.chat"
    exposeSecret("GEMINI_API_KEY")
}

group = "io.github.mee1080.umasim" // Assuming same group as 'core' module
version = "1.0" // Assuming same version as 'core' module

kotlin {
    jvmToolchain(libs.versions.jvmTarget.get().toInt())

    // Define your targets here. For example:
    jvm("desktop")
    js("web", IR) {
        browser()
        useCommonJs() // Or useEsModules()
    }
    // Add other targets like native, android, etc. as needed

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("ai.koog:koog-agents:LATEST_VERSION")
                implementation(compose.runtime)
                implementation(compose.foundation)
                implementation(compose.material)
                implementation(compose.ui) // Added compose.ui for completeness
                implementation("com.russhwolf:multiplatform-settings-no-arg:1.3.0")
                // Potentially add: implementation(compose.components.resources)
                // Potentially add: implementation(compose.components.uiToolingPreview)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(libs.test)
                implementation(libs.test.common)
                implementation(libs.test.annotations)
            }
        }
        // Define source sets for specific targets if needed
        // val desktopMain by getting {
        //     dependencies {
        //         // Desktop-specific dependencies
        //     }
        // }
        // val webMain by getting {
        //     dependencies {
        //         // Web-specific dependencies
        //     }
        // }
    }
}
