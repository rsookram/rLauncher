import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-kapt")
}

android {
    compileSdkVersion(Versions.targetSdk)

    defaultConfig {
        applicationId = "io.github.rsookram.rlauncher"
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = 1
        versionName = "1.0"

        resConfigs("en")
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    dataBinding {
        isEnabled = true
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file("debug.keystore")
            storePassword = "android"
            keyAlias = "androiddebugkey"
            keyPassword = "android"
        }
    }

    buildTypes {
        getByName("debug") {
            signingConfig = signingConfigs.getByName("debug")
        }
        getByName("release") {
            isShrinkResources = true
            isMinifyEnabled = true
            proguardFiles("proguard-rules.pro")

            // Just for testing release builds. Not actually distributed.
            signingConfig = signingConfigs.getByName("debug")
        }
    }

    sourceSets {
        getByName("main").java.srcDirs("src/main/kotlin")
    }

    packagingOptions {
        exclude("/kotlin/**")
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.allWarningsAsErrors = true
}

dependencies {
    implementation(Versions.appCompat)
    implementation(Versions.recyclerView)
    implementation(Versions.ktxCore)
    implementation(Versions.lifecycleExtensions)
    implementation(Versions.lifecycleCommon)
    implementation(Versions.ktxViewModel)

    implementation(Versions.daggerRuntime)
    kapt(Versions.daggerCompiler)

    implementation(Versions.kotlinStdlib)

    testImplementation(Versions.coreTesting)
    testImplementation(Versions.truth)
}
