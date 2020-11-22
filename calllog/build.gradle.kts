plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("kotlin-android")
    id("dagger.hilt.android.plugin")
}

android {
    compileSdkVersion(Apps.compileSdk)
    buildToolsVersion = Apps.buildToolsVersion

    defaultConfig {
        applicationId = "com.biofourmis.ipmpatient"
        minSdkVersion(Apps.minSdk)
        targetSdkVersion(Apps.targetSdk)
        versionCode = Apps.versionCode
        versionName = Apps.versionName
        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("debug") {
            isMinifyEnabled = false
            isDebuggable = true
            applicationIdSuffix = ".debug"
            versionNameSuffix = "-dev"
        }
        getByName("release") {
            isMinifyEnabled = true
            isDebuggable = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    buildFeatures {
        dataBinding = true
        viewBinding = true
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }
}

dependencies {
    implementation(Libs.kotlin)
    implementation(Libs.appcompat)
    implementation(Libs.coreKtx)
    implementation(Libs.constraintLayout)
    implementation(Libs.materialDesign)
    // Multidex
    implementation(Libs.multiDex)
    // Coroutine
    implementation(Libs.coroutineCore)
    implementation(Libs.coroutineAndroid)
    // Lifecycle components
    implementation(Libs.lifecycleComponentExtension)
    kapt(Libs.lifecycleComponentExtension)
    // Viewmodel
    implementation(Libs.viewmodel)
    // Recyclerview
    implementation(Libs.recyclerView)
    // Glide
    implementation(Libs.glide)
    // Lottie
    implementation(Libs.lottie)
    // Acronym view
    implementation(Libs.acronymView)
    // Bottom nav bar
    implementation(Libs.bottomBar)
    // Navigation
    implementation(Libs.navComponentFragment)
    implementation(Libs.navComponentUi)
    // Hilt
    implementation(Libs.hiltCore)
    implementation(Libs.hiltCommon)
    implementation(Libs.hiltViewModelLifecycle)
    kapt(Libs.hiltDaggerAndroidCompiler)
    kapt(Libs.hiltCompiler)

    implementation(project(":commons"))
    implementation(project(":datasource"))

    // Module dependencies
//            implementation deps.commons
//            implementation deps.database
//            implementation deps.system_permissions
//            implementation deps.contacts_provider
//            implementation deps.phone_receiver
//            implementation deps.notifications_provider

}