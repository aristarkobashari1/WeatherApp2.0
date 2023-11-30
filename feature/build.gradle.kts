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

//        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        testInstrumentationRunner = "com.example.feature.ui.HiltTestRunner"
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
    kapt(Dependencies.Hilt.hiltAndroidCompiler)
    Dependencies.AndroidUI.libs.forEach { implementation(it) }
    Dependencies.AndroidX.libs.forEach { implementation(it) }
    Dependencies.Navigation.libs.forEach { implementation(it) }
    Dependencies.Lifecycle.libs.forEach { implementation(it) }
    Dependencies.Retrofit.libs.forEach { implementation(it) }


    implementation(project(Dependencies.Modules.data))
    implementation(project(Dependencies.Modules.common))
    implementation(project(Dependencies.Modules.model))
    implementation(project(Dependencies.Modules.database))

///////////
    testImplementation("com.squareup.okhttp3:mockwebserver:4.10.0")
    testImplementation("com.google.truth:truth:1.1.5")
    testImplementation("junit:junit:4.13.2")
    testImplementation("io.mockk:mockk:1.13.8")

    androidTestImplementation("com.google.truth:truth:1.1.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.datastore:datastore-preferences:1.0.0")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.44")
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.44")
    androidTestImplementation(Dependencies.Room.room)
    kaptAndroidTest(Dependencies.Room.roomCompiler)
    androidTestImplementation("app.cash.turbine:turbine:0.7.0") //testing flows
    androidTestImplementation("androidx.test:runner:1.5.2")
    debugImplementation("androidx.fragment:fragment-testing:1.6.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("com.adevinta.android:barista:4.2.0") {
        exclude( group= "org.jetbrains.kotlin" )// Only if you already use Kotlin in your project
    }



    ///////////


    //Todo refactor late
    val epoxyVersion = "5.1.1"
    implementation("com.airbnb.android:epoxy:$epoxyVersion")
    implementation("com.airbnb.android:epoxy-databinding:$epoxyVersion")
    implementation("com.airbnb.android:epoxy-paging3:$epoxyVersion")
    kapt("com.airbnb.android:epoxy-processor:$epoxyVersion")


    implementation("com.google.android.gms:play-services-location:21.0.1")
    implementation("com.google.android.gms:play-services-maps:18.1.0")
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    implementation("com.github.bumptech.glide:glide:4.16.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.2.0-alpha01")
    //



}
repositories {
    google()
}
