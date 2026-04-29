plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.savlukov.app.feature.ar"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation("javax.inject:javax.inject:1")
    implementation(project(":core:domain"))
    implementation(project(":core:ui"))
    implementation("org.osmdroid:osmdroid-android:6.1.18")

    implementation(libs.androidx.core.ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    implementation("com.google.android.filament:filament-android:1.70.0")
    implementation(libs.ar.core)

    implementation(libs.coil.compose)

    testImplementation(libs.turbine)
    testImplementation(libs.junit)
}
