plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "1.9.20-1.0.14"
}

android {
    namespace = "com.chronelab.madas2schoolconnectapp"
    compileSdk = 34

    defaultConfig {
        applicationId; "com.chronelab.madas2schoolconnectapp"
        minSdk = 24
        targetSdk = 34
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    // Import the Compose BOM
    implementation(platform(libs.androidx.compose.bom.v20240600))
    implementation(libs.androidx.activity.compose.v191)
    implementation(libs.material3)
    implementation(libs.ui)
    implementation(libs.ui.tooling)
    implementation(libs.ui.tooling.preview)
    implementation(libs.androidx.lifecycle.runtime.ktx.v284)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation (libs.androidx.compose.ui.ui)
    implementation (libs.androidx.compose.material3.material3)
    implementation (libs.androidx.material.icons.extended)
    implementation(libs.androidx.media3.common)
    testImplementation(libs.junit.junit)
    dependencies {
        implementation (libs.androidx.core.ktx)
        implementation (libs.androidx.lifecycle.runtime.ktx.v284)
        implementation (libs.androidx.activity.compose.v191)
        implementation (libs.androidx.compose.ui.ui)
        implementation (libs.androidx.compose.material3.material3)
        implementation (libs.androidx.material.icons.extended)
        implementation (libs.androidx.compose.ui.ui.tooling.preview)
        implementation (libs.androidx.lifecycle.viewmodel.compose.v284)
        implementation (libs.androidx.lifecycle.runtime.compose)
        implementation (libs.androidx.navigation.compose)
        implementation (libs.kotlinx.coroutines.android)
        implementation (libs.androidx.material.icons.extended)
        implementation (libs.androidx.runtime.livedata)
        implementation (libs.gson)

    }

    // Testing
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)

    //Room
    implementation(libs.androidx.room.runtime)
    ksp("androidx.room:room-compiler:${rootProject.extra["room_version"]}")
    implementation(libs.androidx.room.ktx)
}

