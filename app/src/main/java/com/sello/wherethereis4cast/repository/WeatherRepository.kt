package com.sello.wherethereis4cast.repository

import android.util.Log
import com.sello.wherethereis4cast.data.DataOrException
import com.sello.wherethereis4cast.model.Weather
import com.sello.wherethereis4cast.network.WeatherAPI
import javax.inject.Inject

class WeatherRepository @Inject constructor(private val api: WeatherAPI) {
    suspend fun getWeatherUpdate(
        locationOfCity: String
    ): DataOrException<Weather, Boolean, Exception> {

        val response = try {
            api.getWeatherUpdate(location = locationOfCity)
        } catch (e: Exception) {
            Log.d("CAUGHT", "getWeatherUpdate: $e")
            return DataOrException(exception = e)
        }

        Log.d("SUCCESSFUL", "getWeatherUpdate: $response")
        return DataOrException(data = response)
    }
}