plugins {
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.savlukov.app.feature.stories"
    compileSdk = 35

    defaultConfig {
        minSdk = 24
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
    implementation(libs.javax.inject)
    implementation(project(":core:domain"))
    implementation(project(":core:ui"))
    
    implementation(libs.androidx.core-ktx)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.androidx.lifecycle.viewmodel-ktx)
    
    implementation(libs.kotlinx.coroutines.android)
    
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)
    implementation(libs.coil.compose)
}
