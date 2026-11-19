plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
}

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

    // Menghubungkan folder model dari project utama SINEMAKU
    sourceSets {
        getByName("main") {
            java.srcDir("../../src/main/kotlin/model")
        }
    }

    buildFeatures {
        compose = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21  // ✅ ubah dari VERSION_11
        targetCompatibility = JavaVersion.VERSION_21  // ✅ ubah dari VERSION_11
    }

    kotlinOptions {
        jvmTarget = "21"  // ✅ ubah dari "11"
    }
}

dependencies {
    implementation(libs.core.ktx)
    implementation(libs.activity.compose)

    implementation(platform(libs.compose.bom))
    implementation(libs.compose.ui)
    implementation(libs.compose.ui.tooling.preview)
    implementation(libs.compose.material3)

    implementation(libs.material.icons.extended)

    implementation(libs.media3.common)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.ui)

    debugImplementation(libs.compose.ui.tooling)

    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
}