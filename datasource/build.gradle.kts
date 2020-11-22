plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(Apps.compileSdk)

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
}

dependencies {
    implementation(Libs.kotlin)
    implementation(Libs.appcompat)
    implementation(Libs.coreKtx)
    implementation(Libs.constraintLayout)
    // Hilt
    implementation(Libs.hiltCore)
    implementation(Libs.hiltCommon)
    implementation(Libs.hiltViewModelLifecycle)
    kapt(Libs.hiltDaggerAndroidCompiler)
    kapt(Libs.hiltCompiler)
    // Room
    api(Libs.roomRuntime)
    api(Libs.roomKts)
    kapt(Libs.roomCompiler)

    implementation(project(":commons"))
}