object Apps {
    const val compileSdk = 30
    const val buildToolsVersion = "30.0.2"
    const val minSdk = 21
    const val targetSdk = 29
    const val versionCode = 8
    const val versionName = "1.0.0"
}

object Versions {
    const val gradle = "4.1.0"
    const val kotlin = "1.4.10"
    const val appcompat = "1.2.0"
    const val coreKtx = "1.3.2"
    const val constraintLayout = "2.0.2"
    const val recyclerView = "1.1.0"
    const val navComponent = "2.3.0"
    const val hilt = "2.28-alpha"
    const val hiltSnapShot = "1.0.0-SNAPSHOT"
    const val retrofit = "2.9.0"
    const val moshi = "1.9.2"
    const val gson = "2.8.6"
    const val room = "2.2.5"
    const val coroutine = "1.3.5"
    const val materialDesign = "1.2.0"
    const val lottie = "3.4.0"
    const val lifecycle = "1.1.1"
    const val viewmodel = "2.3.0-alpha01"
    const val glide = "4.8.0"
    const val multidex = "2.0.1"
    const val smartTabLayout= "2.0.0@aar"
    const val materialDialog = "3.3.0"
    const val acronymView = "2.0"
    const val bottomBar = "0.2"
    const val viewPager = "1.0.0"
    const val fabulousFilter = "0.0.5"
    const val fuzzySearch = "1.3.1"
    // Test
    const val junit = "4.12"
    const val junit_extension = "1.1.2"
    const val espresso_core = "3.3.0"
}

object Libs {
    const val kotlin = "org.jetbrains.kotlin:kotlin-stdlib-jdk7:${Versions.kotlin}"
    const val appcompat = "androidx.appcompat:appcompat:${Versions.appcompat}"
    const val coreKtx = "androidx.core:core-ktx:${Versions.coreKtx}"
    const val constraintLayout =
        "androidx.constraintlayout:constraintlayout:${Versions.constraintLayout}"
    const val recyclerView = "androidx.recyclerview:recyclerview:${Versions.recyclerView}"

    // Hilt
    const val hiltCore = "com.google.dagger:hilt-android:${Versions.hilt}"
    const val hiltDaggerAndroidCompiler = "com.google.dagger:hilt-android-compiler:${Versions.hilt}"
    const val hiltCommon = "androidx.hilt:hilt-common:${Versions.hiltSnapShot}"
    const val hiltViewModelLifecycle =
        "androidx.hilt:hilt-lifecycle-viewmodel:${Versions.hiltSnapShot}"
    const val hiltCompiler = "androidx.hilt:hilt-compiler:${Versions.hiltSnapShot}"

    // Navigation component
    const val navComponentFragment =
        "androidx.navigation:navigation-fragment-ktx:${Versions.navComponent}"
    const val navComponentUi = "androidx.navigation:navigation-ui-ktx:${Versions.navComponent}"

    // Retrofit and Mosi
    const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.retrofit}"
    const val moshiConverter = "com.squareup.retrofit2:converter-moshi:${Versions.retrofit}"
    const val moshi = "com.squareup.moshi:moshi-kotlin:${Versions.moshi}"
    const val moshiCodeGen = "com.squareup.moshi:moshi-kotlin-codegen:${Versions.moshi}"

    // Gson
    const val gson = "com.google.code.gson:gson:${Versions.gson}"

    // Room Database
    const val roomRuntime = "androidx.room:room-runtime:${Versions.room}"
    const val roomKts = "androidx.room:room-ktx:${Versions.room}"
    const val roomCompiler = "androidx.room:room-compiler:${Versions.room}"

    const val materialDesign = "com.google.android.material:material:${Versions.materialDesign}"
    const val lottie = "com.airbnb.android:lottie:${Versions.lottie}"
    const val viewmodel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.viewmodel}"
    const val glide = "com.github.bumptech.glide:glide:${Versions.glide}"
    const val multiDex = "androidx.multidex:multidex:${Versions.multidex}"
    const val bottomBar = "com.github.iammert:ReadableBottomBar:${Versions.bottomBar}"
    const val acronymView = "com.redmadrobot:acronym-avatar:${Versions.acronymView}"

    const val coroutineCore = "org.jetbrains.kotlinx:kotlinx-coroutines-core:${Versions.coroutine}"
    const val coroutineAndroid =
        "org.jetbrains.kotlinx:kotlinx-coroutines-android:${Versions.coroutine}"
    const val coroutineTest = "org.jetbrains.kotlinx:kotlinx-coroutines-test:${Versions.coroutine}"

    const val lifecycleComponentExtension =
        "android.arch.lifecycle:extensions:${Versions.lifecycle}"
    const val lifecycleComponentCompiler = "android.arch.lifecycle:compiler:${Versions.lifecycle}"
    const val lifecycleComponentTestHelper = "android.arch.core:core-testing:${Versions.lifecycle}"

    const val smartTabLayoutUtils= "com.ogaclejapan.smarttablayout:utils-v4:${Versions.smartTabLayout}"

    const val materialDialogCore = "com.afollestad.material-dialogs:core:${Versions.materialDialog}"
    const val materialDialogInput =
        "com.afollestad.material-dialogs:input:${Versions.materialDialog}"

    const val viewPager = "androidx.viewpager2:viewpager2:${Versions.viewPager}"

    const val fabulousBottomBar = "com.allattentionhere:fabulousfilter:${Versions.fabulousFilter}"

    const val fuzzySearch = "me.xdrop:fuzzywuzzy:${Versions.fuzzySearch}"

}

object Modules {
    const val commons = ":commons"
    const val datasource = ":datasource"
}

object TestLibs {
    const val junit = "junit:junit:${Versions.junit}"
    const val junitExtension = "androidx.test.ext:junit:${Versions.junit_extension}"
    const val espressoCore = "androidx.test.espresso:espresso-core:${Versions.espresso_core}"
}


