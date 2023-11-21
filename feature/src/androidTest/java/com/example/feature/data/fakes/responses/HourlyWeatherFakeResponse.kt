package com.example.feature.data.fakes.responses

object HourlyWeatherFakeResponse {
    val fakeJsonResponse = """
        {
            "cod": "200",
            "message": 0,
            "cnt": 3,
            "list": [
                {
                    "base": "fakeBase",
                    "clouds": {
                        "all": 10
                    },
                    "cod": 200,
                    "coord": {
                        "lat": 0.0,
                        "lon": 0.0
                    },
                    "dt": 1636170000,
                    "id": 123,
                    "main": {
                        "temp": 25.5,
                        "feels_like": 26.0,
                        "temp_min": 24.0,
                        "temp_max": 27.0,
                        "pressure": 1010,
                        "humidity": 70,
                        "sea_level": 1010,
                        "grnd_level": 1009
                    },
                    "name": "FakeCity",
                    "rain": {
                        "1h": 0.0
                    },
                    "sys": {
                        "country": "FakeCountry",
                        "sunrise": 1636123456,
                        "sunset": 1636167890
                    },
                    "timezone": 0,
                    "visibility": 10000,
                    "weather": [
                        {
                            "id": 800,
                            "main": "Clear",
                            "description": "clear sky",
                            "icon": "01d"
                        }
                    ],
                    "wind": {
                        "speed": 5.0,
                        "deg": 180
                    }
                },
            ]
        }
    """.trimIndent()
}
