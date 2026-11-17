
plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("org.jetbrains.kotlin.plugin.compose")
}

// ==========================================
// LOKASI MODEL DOMAIN FAWWAZ
// ==========================================

// Root project Android:
// D:/Android Studio/Project/Cinemaku/android-app
//
// Folder model Fawwaz:
// D:/Android Studio/Project/Cinemaku/src/main/kotlin/model

val sharedModelDirectory =
    rootProject.file("../src/main/kotlin/model")

// Memastikan folder model tersedia.
check(sharedModelDirectory.isDirectory) {
    "Folder model Fawwaz tidak ditemukan: " +
            sharedModelDirectory.absolutePath
}

println(
    "SINEMAKU shared model: " +
            sharedModelDirectory.absolutePath
)

// ==========================================
// KONFIGURASI ANDROID
// ==========================================

android {

    namespace = "com.example.sinemaku"

    compileSdk = 36

    defaultConfig {

        applicationId = "com.example.sinemaku"

        minSdk = 24
        targetSdk = 36

        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner =
            "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {

        release {

            isMinifyEnabled = false

            proguardFiles(
                getDefaultProguardFile(
                    "proguard-android-optimize.txt"
                ),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {

        sourceCompatibility =
            JavaVersion.VERSION_17

        targetCompatibility =
            JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }

    // ======================================
    // SOURCE SET MODEL FAWWAZ
    // ======================================

    sourceSets {

        getByName("main") {

            java.srcDir(sharedModelDirectory)
        }
    }
}

// ==========================================
// KONFIGURASI KOTLIN
// ==========================================

kotlin {
    jvmToolchain(17)
}

// ==========================================
// DEPENDENCIES
// ==========================================

dependencies {

    // Android Core
    implementation(
        "androidx.core:core-ktx:1.15.0"
    )

    // Activity Compose
    implementation(
        "androidx.activity:activity-compose:1.10.1"
    )

    // Compose BOM
    implementation(
        platform(
            "androidx.compose:compose-bom:2025.04.01"
        )
    )

    // Compose UI
    implementation(
        "androidx.compose.ui:ui"
    )

    implementation(
        "androidx.compose.ui:ui-tooling-preview"
    )

    // Compose Foundation
    implementation(
        "androidx.compose.foundation:foundation"
    )

    // Material Design 3
    implementation(
        "androidx.compose.material3:material3"
    )

    // Lifecycle
    implementation(
        "androidx.lifecycle:lifecycle-runtime-ktx:2.9.0"
    )

    // Unit Testing
    testImplementation(
        "junit:junit:4.13.2"
    )

    // Android Testing
    androidTestImplementation(
        "androidx.test.ext:junit:1.2.1"
    )

    androidTestImplementation(
        "androidx.test.espresso:espresso-core:3.6.1"
    )

    // Compose UI Testing
    androidTestImplementation(
        platform(
            "androidx.compose:compose-bom:2025.04.01"
        )
    )

    androidTestImplementation(
        "androidx.compose.ui:ui-test-junit4"
    )

    // Compose Debugging
    debugImplementation(
        "androidx.compose.ui:ui-tooling"
    )

    debugImplementation(
        "androidx.compose.ui:ui-test-manifest"
    )
}
