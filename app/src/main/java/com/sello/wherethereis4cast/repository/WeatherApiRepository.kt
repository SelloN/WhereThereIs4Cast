package com.sello.wherethereis4cast.repository

import android.util.Log
import com.sello.wherethereis4cast.data.WeatherDataPOJO
import com.sello.wherethereis4cast.model.Weather
import com.sello.wherethereis4cast.network.WeatherAPI
import javax.inject.Inject

class WeatherApiRepository @Inject constructor(private val api: WeatherAPI) {
    suspend fun getWeatherUpdate(
        locationOfCity: String,
        currentState: WeatherDataPOJO<Weather, Exception>
    ): WeatherDataPOJO<Weather, Exception> {
        return try {
            val response = api.getWeatherUpdate(location = locationOfCity)
            Log.d("SUCCESSFUL", "getWeatherUpdate: $response")
            currentState.copy(
                data = response,
                isSearchedFromTextFieldLocationFound = true,
                loading = false,
                exception = null
            )

        } catch (e: Exception) {
            Log.d("CAUGHT", "getWeatherUpdate: $e")
            currentState.copy(
                exception = e,
                isSearchedFromTextFieldLocationFound = false)
        }
    }

    suspend fun getWeatherUpdate(
        latitude: Double,
        longitude: Double,
        currentState: WeatherDataPOJO<Weather, Exception>
    ): WeatherDataPOJO<Weather, Exception> {

        return try {
            val response = api.getWeatherUpdate(latitude, longitude)
            Log.d("SUCCESSFUL", "getWeatherUpdate: $response")
            currentState.copy(
                data = response,
                loading = false
            )

        } catch (e: Exception) {
            Log.d("CAUGHT", "getWeatherUpdate using coordinates: $e")
            currentState.copy(exception = e, loading = false)
        }
    }
}