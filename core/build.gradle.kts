plugins {
    kotlin("multiplatform")
}

group = "io.github.mee1080.umasim"
version = "1.0"

kotlin {

    jvm("desktop") {
        compilations.all {
            kotlinOptions.jvmTarget = "16"
        }
    }

    js("web",IR) {
        useCommonJs()
        browser()
    }

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(kotlin("stdlib-common"))
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }
    }
}