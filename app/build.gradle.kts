plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.gms.google-services")
}
android {
    signingConfigs {
        getByName("debug") {
            storeFile = file("D:\\tai-lieu-uit\\nam-3\\Mobile\\keystore_path\\AppKeyStore2.jks")
            storePassword = "123456"
            keyAlias = "key0"
            keyPassword = "123456"
        }
    }
    namespace = "com.uit.chiechnonkydieu"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.uit.chiecnonkydieu"
        minSdk = 24
        targetSdk = 34
        versionCode = 2
        versionName = "2.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    viewBinding {
        enable = true
    }
    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.firebase.database)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    //coroutine
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.0")

    //wheel
    implementation("androidx.navigation:navigation-dynamic-features-fragment:2.7.7")
    implementation ("com.github.thanhniencung:LuckyWheel:a6110f5128")

    // DataStore
    implementation("androidx.datastore:datastore-preferences:1.0.0")

    // gemini
    implementation("com.google.android.material:material:1.11.0")
    implementation("com.google.ai.client.generativeai:generativeai:0.4.0")

    // google pay API
    val lifecycleVersion = "2.7.0"

    implementation("com.google.android.gms:play-services-wallet:19.3.0-beta01")
    implementation("com.google.pay.button:compose-pay-button:0.1.3")


    implementation("androidx.compose.foundation:foundation")
    implementation("androidx.compose.material3:material3")

    val composeBom = platform("androidx.compose:compose-bom:2024.05.00")
    implementation(composeBom)

    debugImplementation("androidx.compose.ui:ui-tooling")
    implementation("androidx.compose.ui:ui-tooling-preview")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.activity:activity-compose:1.8.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:$lifecycleVersion")
    implementation("androidx.lifecycle:lifecycle-runtime-compose:$lifecycleVersion")
    implementation("androidx.activity:activity-ktx:1.8.2")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.1")

    androidTestImplementation("androidx.test.uiautomator:uiautomator:2.2.0")
    androidTestImplementation("androidx.compose.ui:ui-test-junit4:1.6.0")

    // coil
    implementation ("io.coil-kt:coil:2.1.0")

    // retrofit
    implementation("com.jakewharton.retrofit:retrofit2-kotlinx-serialization-converter:1.0.0")
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    // gson
    implementation ("com.google.code.gson:gson:2.10.1")

    // ads
    implementation ("com.google.android.gms:play-services-ads:23.1.0")

}