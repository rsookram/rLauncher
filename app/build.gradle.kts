import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("com.android.application")
    kotlin("android")
}

android {
    registerTransform(RemoveClassTransform())

    compileSdkVersion(Versions.targetSdk)

    defaultConfig {
        applicationId = "io.github.rsookram.rlauncher"
        minSdkVersion(Versions.minSdk)
        targetSdkVersion(Versions.targetSdk)
        versionCode = 1
        versionName = "1.0"

        resConfigs("en", "anydpi")
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
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
        exclude("META-INF/*.kotlin_module")
        exclude("META-INF/*.version")
    }

    lintOptions {
        disable(
            "GoogleAppIndexingWarning", // This app doesn't need to be indexed
            "SyntheticAccessor" // R8 handles optimizing these away
        )

        isCheckGeneratedSources = true
        isCheckAllWarnings = true

        isWarningsAsErrors = true
    }
}

tasks.withType<KotlinCompile> {
    kotlinOptions.allWarningsAsErrors = true
}

dependencies {
    implementation(Versions.activity)
    implementation(Versions.recyclerView)
    implementation(Versions.ktxCore)
    implementation(Versions.lifecycleCommon)

    testImplementation(Versions.coreTesting)
    testImplementation(Versions.truth)
}
