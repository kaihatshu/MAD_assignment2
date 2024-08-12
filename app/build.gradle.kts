plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.devtools.ksp") version "1.9.20-1.0.14"
}

android {
    namespace = "com.chronelab.roomdatabase"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.chronelab.roomdatabase"
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
    implementation(platform("androidx.compose:compose-bom:2023.10.01"))
    implementation("androidx.activity:activity-compose:1.8.1")
    implementation("androidx.compose.material3:material3")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.2")
    implementation("androidx.navigation:navigation-compose:2.7.5")
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation ("androidx.compose.ui:ui:1.1.0")
    implementation ("androidx.compose.material3:material3:1.0.0-alpha12")
    implementation ("androidx.compose.material:material-icons-extended:1.1.0")
    testImplementation(libs.junit.junit)
    dependencies {
        implementation ("androidx.core:core-ktx:1.9.0")
        implementation ("androidx.lifecycle:lifecycle-runtime-ktx:2.5.1")
        implementation ("androidx.activity:activity-compose:1.6.1")
        implementation ("androidx.compose.ui:ui:1.3.2")
        implementation ("androidx.compose.material3:material3:1.0.0-alpha12")
        implementation ("androidx.compose.material:material-icons-extended:1.3.2")
        implementation ("androidx.compose.ui:ui-tooling-preview:1.3.2")
        implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.5.1")
        implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.5.1")
        implementation ("androidx.navigation:navigation-compose:2.5.2")
        implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")
        implementation ("androidx.compose.material:material-icons-extended:1.4.3")
        implementation ("androidx.compose.runtime:runtime-livedata:1.4.3")
        implementation ("com.google.code.gson:gson:2.8.9")

    }

    // Testing
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")

    //Room
    implementation("androidx.room:room-runtime:${rootProject.extra["room_version"]}")
    ksp("androidx.room:room-compiler:${rootProject.extra["room_version"]}")
    implementation("androidx.room:room-ktx:${rootProject.extra["room_version"]}")
}

