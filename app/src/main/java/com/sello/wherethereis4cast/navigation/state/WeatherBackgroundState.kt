package com.sello.wherethereis4cast.navigation.state

enum class WeatherBackgroundState {
    Clear,
    Clouds,
    Rain,
    Snow;

    companion object {

        fun getBackgroundValue(value: String): Pair<String, String> {
            return when (value) {
                Clear.name -> {
                    Pair("clear_background", "sunny_blue")
                }

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
                    //will never be a case
                    Pair(String(), String())
                }
            }
        }

    }
}