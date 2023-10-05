plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
}

android {
    namespace = "com.example.data"
    compileSdk = Config.targetSdk

    defaultConfig {
        minSdk = Config.minSdk
        targetSdk = Config.targetSdk

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        consumerProguardFiles("consumer-rules.pro")
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {

    Dependencies.Retrofit.libs.forEach { implementation(it) }

    implementation(project(Dependencies.Modules.model))
    implementation(project(Dependencies.Modules.network))
    implementation(project(Dependencies.Modules.database))
    implementation(project(Dependencies.Modules.common))
    implementation(Dependencies.Hilt.hiltAndroid)
    kapt(Dependencies.Hilt.hiltAndroidCompiler)

    implementation("androidx.datastore:datastore-preferences:1.0.0")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.6.4")

    testImplementation(Dependencies.Test.junit)
    androidTestImplementation(Dependencies.Test.testJunit)
    androidTestImplementation(Dependencies.Test.espresso)
    testImplementation(Dependencies.Test.truth)
}