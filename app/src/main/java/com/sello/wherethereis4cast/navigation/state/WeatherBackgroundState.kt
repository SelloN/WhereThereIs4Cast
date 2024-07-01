package com.sello.wherethereis4cast.navigation.state

enum class WeatherBackgroundState {
    Clear,
    Clouds,
    Rain,
    Snow;

    companion object {
        private val values = entries.toTypedArray()

        fun getBackgroundValue(value: String): String {

            for (i in values) {
                return when (i) {
                    Clear -> {
                        "clear_background"
                    }

                    Clouds -> {
                        "cloudy_background"
                    }

                    Rain -> {
                        "rain_background"
                    }

                    Snow -> {
                        "rain_background"
                    }
                }
            }
            return "No specified weather condition that is handles in our code. " +
                    "Please check weatherItem.main"
        }
    }
}