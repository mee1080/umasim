plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.kotlin.serialization)
}

group = "io.github.mee1080.umasim"
version = "1.0"

kotlin {

    jvmToolchain(libs.versions.jvmTarget.get().toInt())

    jvm()

    js {
        useCommonJs()
        browser()
    }

    sourceSets {
        commonMain.dependencies {
            implementation(project(":utility"))
            implementation(libs.kotlinx.serializationJson)
            implementation(libs.kotlinx.coroutinesCore)
        }
        commonTest.dependencies {
            implementation(libs.test)
            implementation(libs.test.common)
            implementation(libs.test.annotations)
            implementation(libs.kotlinx.coroutinesTest)
        }
    }
}
