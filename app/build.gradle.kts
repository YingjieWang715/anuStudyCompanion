plugins {
    alias(libs.plugins.androidApplication)
    id("com.google.gms.google-services")
}

android {
    namespace = "au.edu.anu.comp6442.group03.studyapp"
    compileSdk = 34

    defaultConfig {
        applicationId = "au.edu.anu.comp6442.group03.studyapp"
        minSdk = 29
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
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
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    implementation(libs.firebase.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.ui.firestore)
    implementation(libs.gson)
    implementation(libs.firebase.database)
    implementation(libs.ext.junit)
    testImplementation(libs.junit.v413)
    testImplementation(libs.mockito.core)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.flexbox)
    implementation(libs.mpandroidchart)
}
