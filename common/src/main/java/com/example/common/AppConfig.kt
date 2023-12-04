package com.example.common



enum class Language(val lang: String) {
    ENG("en"),
    ALB("sq"),
    IT("it"),
    GR("el")
}

enum class Units(val value: String){
    METRIC("metric"),
    IMPERIAL("imperial"),
    CELCIUS(" \u2103"),
    KELVIN(" K"),
    FAHRENHEIT(" \u2109"),
    METRE(" m/s"),
    MILES(" mph/h")
}


 object Geocode{
        const val LOCATION_PERMISSION_REQUEST_CODE = 1001
        const val LOCATION_UPDATE_INTERVAL = 10000L // 10 seconds
        const val LOCATION_UPDATE_FASTEST_INTERVAL = 5000L // 5 seconds
}

object Api{
    const val API_KEY = "c18b263ccb35b8c930c109b9edc01349"
}

object Google{
    const val RC_SIGN_IN = 999
    const val WEB_CLIENT_ID = "215930187505-fn8bn3rsgq2j7it7ag61kogmdmfqmqia.apps.googleusercontent.com"
}