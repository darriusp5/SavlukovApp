plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.hilt)
    alias(libs.plugins.kotlin.kapt)
}

android {
    namespace = "com.savlukov.app"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.savlukov.app"
        minSdk = 26
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    lint {
        disable.add("NullSafeMutableLiveData")
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    buildFeatures {
        compose = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
        jniLibs.pickFirsts.add("lib/*/libfilament-jni.so")
    }
}

dependencies {
    implementation("javax.inject:javax.inject:1")
    implementation(project(":core:domain"))
    implementation(project(":core:data"))
    implementation(project(":core:ui"))

    implementation(project(":features:stories"))
    implementation(project(":features:navigation"))
    implementation(project(":features:catalog"))
    implementation(project(":features:product"))
    implementation(project(":features:home"))
    implementation(project(":features:ar")) // Assuming ar module exists

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.hilt.android)
    implementation(libs.androidx.ui.test.junit4.android)
    kapt(libs.hilt.compiler)
    implementation("io.coil-kt.coil3:coil-compose:3.0.4")
    implementation("io.coil-kt.coil3:coil-network-okhttp:3.0.4")
    // Firebase & Notifications
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.messaging)
    implementation("com.yandex.android:maps.mobile:4.33.1-navikit")
    // WorkManager for Sync
    implementation(libs.androidx.work.runtime)

    // In-App Updates
    implementation("com.google.android.play:app-update:2.1.0")

    // YAML Parsing
    implementation("com.charleskorn.kaml:kaml:0.55.0")







    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.turbine)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}
