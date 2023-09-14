object Dependencies {

    interface Group {
        val libs: List<String>
    }

    object AndroidX : Group {
        private const val core = "androidx.core:core-ktx:1.10.0"
        private const val legacy_support = "androidx.legacy:legacy-support-v4:1.0.0"
        override val libs: List<String>
            get() = listOf(core, legacy_support)
    }

    object Google{
        const val play_core = "com.google.android.play:core:${Versions.Google.playCore}"
        const val play_services = "com.google.android.gms:play-services-location:${Versions.Google.playService}"
    }

    object AndroidUI : Group {
         const val appcompat = "androidx.appcompat:appcompat:${Versions.AndroidUI.appCompat}"
         const val material = "com.google.android.material:material:${Versions.Material.version}"
         const val constraintLayout = "androidx.constraintlayout:constraintlayout:${Versions.AndroidUI.constraintLayout}"

        override val libs: List<String>
            get() = listOf(appcompat, material, constraintLayout)
    }


    object AndroidTest : Group {
        const val junit = "junit:junit:${Versions.Test.junit}"
        const val ext_junit = "androidx.test.ext:junit:${Versions.Test.ext_junit}"
        const val espresso_core =
            "androidx.test.espresso:espresso-${Versions.Test.espresso_core}"

        override val libs: List<String>
            get() = listOf(ext_junit, espresso_core)

    }

    object Navigation : Group {
        private const val navigation_fragment =
            "androidx.navigation:navigation-fragment-ktx:${Versions.Navigation.navigation}"
        private const val navigation_ui =
            "androidx.navigation:navigation-ui-ktx:${Versions.Navigation.navigation}"

        override val libs: List<String>
            get() = listOf(navigation_fragment, navigation_ui)
    }

    object Modules : Group {
        const val feature= ":feature"
        const val data = ":data"
        const val model =":model"
        const val database=":database"
        const val network=":network"
        const val common=":common"

        override val libs: List<String>
            get() = listOf(data, feature, model, database, network, common)

    }

    object Lifecycle : Group{
        val livedata = "androidx.lifecycle:lifecycle-livedata-ktx:${Versions.Lifecycle.lifecycle}"
        val viewModel = "androidx.lifecycle:lifecycle-viewmodel-ktx:${Versions.Lifecycle.lifecycle}"
        override val libs: List<String>
            get() = listOf(livedata, viewModel)
    }

    object Hilt : Group{
        const val hiltAndroid = "com.google.dagger:hilt-android:${Versions.DI.hiltVer}"
        const val hiltAndroidCompiler = "com.google.dagger:hilt-compiler:${Versions.DI.hiltVer}"
        override val libs: List<String>
            get() = listOf(hiltAndroid, hiltAndroidCompiler)
    }

    object Retrofit: Group{
        const val retrofit = "com.squareup.retrofit2:retrofit:${Versions.Retrofit.retrofit}"
        const val gsonConverter = "com.squareup.retrofit2:converter-gson:${Versions.Retrofit.retrofit}"
        override val libs: List<String>
            get() = listOf(retrofit, gsonConverter)
    }

    object Room: Group{
        const val room = "androidx.room:room-ktx:${Versions.Room.roomVersion}"
        const val roomCompiler = "androidx.room:room-compiler:${Versions.Room.roomVersion}"
        const val roomCommon = "androidx.room:room-common:${Versions.Room.roomVersion}"

        override val libs: List<String>
            get() = listOf(room, roomCompiler, roomCommon)
    }



}