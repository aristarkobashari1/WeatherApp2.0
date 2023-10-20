plugins {
    id("com.android.library")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id ("androidx.navigation.safeargs")

}

android {
    namespace = "com.example.feature"
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

    buildFeatures {
        viewBinding = true
        dataBinding = true
    }


}

dependencies {

    implementation(Dependencies.Hilt.hiltAndroid)
    implementation("androidx.test:monitor:1.6.1")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    androidTestImplementation("junit:junit:4.12")
    kapt(Dependencies.Hilt.hiltAndroidCompiler)
    Dependencies.AndroidUI.libs.forEach { implementation(it) }
    Dependencies.AndroidX.libs.forEach { implementation(it) }
    Dependencies.Navigation.libs.forEach { implementation(it) }
    Dependencies.Lifecycle.libs.forEach { implementation(it) }
    Dependencies.Retrofit.libs.forEach { implementation(it) }

//    testImplementation("junit:junit:4.13.2")
//    androidTestImplementation("androidx.test.ext:junit:1.1.5")
//    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation(project(Dependencies.Modules.data))
    implementation(project(Dependencies.Modules.common))
    implementation(project(Dependencies.Modules.model))
    implementation(project(Dependencies.Modules.database))

    val epoxyVersion = "5.1.1"
    implementation("com.airbnb.android:epoxy:$epoxyVersion")
    implementation("com.airbnb.android:epoxy-databinding:$epoxyVersion")
    implementation("com.airbnb.android:epoxy-paging3:$epoxyVersion")
    kapt("com.airbnb.android:epoxy-processor:$epoxyVersion")


    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    implementation("com.github.bumptech.glide:glide:4.16.0")

}
repositories {
    google()
}
