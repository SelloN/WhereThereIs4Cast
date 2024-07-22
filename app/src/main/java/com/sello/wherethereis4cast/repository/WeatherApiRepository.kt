package com.sello.wherethereis4cast.repository

import android.util.Log
import com.sello.wherethereis4cast.data.DataOrException
import com.sello.wherethereis4cast.model.Weather
import com.sello.wherethereis4cast.network.WeatherAPI
import javax.inject.Inject

class WeatherApiRepository @Inject constructor(private val api: WeatherAPI) {
    suspend fun getWeatherUpdate(
        locationOfCity: String
    ): DataOrException<Weather, Boolean, Exception> {

        val response = try {
            api.getWeatherUpdate(location = locationOfCity)
        } catch (e: Exception) {
            Log.d("CAUGHT", "getWeatherUpdate: $e")
            return DataOrException(exception = e,
                isSearchedFromTextFieldLocationFound = false)
        }

        Log.d("SUCCESSFUL", "getWeatherUpdate: $response")
        return DataOrException(data = response, isSearchedFromTextFieldLocationFound = true, loading = false)
    }

    suspend fun getWeatherUpdate(latitude: Double, longitude: Double):
            DataOrException<Weather, Boolean, Exception> {

        val response = try {
            api.getWeatherUpdate(latitude, longitude)
        } catch (e: Exception) {
            Log.d("CAUGHT", "getWeatherUpdate using coordinates: $e")
            return DataOrException(exception = e, loading = false)
        }

        Log.d("SUCCESSFUL", "getWeatherUpdate: $response")
        return DataOrException(data = response, isSearchedFromTextFieldLocationFound = false, loading = false)
    }
}