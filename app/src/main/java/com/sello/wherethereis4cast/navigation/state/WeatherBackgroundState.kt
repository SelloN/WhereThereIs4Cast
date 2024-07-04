package com.sello.wherethereis4cast.navigation.state

enum class WeatherBackgroundState {
    Clouds,
    Rain,
    Snow;

    companion object {

        fun getBackgroundValue(value: String): Pair<String, String> {
            return when (value) {
                Clouds.name -> {
                    Pair("cloudy_background", "cloudy_grey")
                }

                Rain.name -> {
                    Pair("rainy_background", "rainy_grey")
                }

                Snow.name -> {
                    Pair("rainy_background", "rainy_grey")
                }

                else -> {
                    Pair("clear_background", "sunny_blue")
                }
            }
        }

    }
}