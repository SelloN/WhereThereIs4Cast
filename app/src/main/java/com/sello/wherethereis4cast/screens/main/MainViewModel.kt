package com.sello.wherethereis4cast.screens.main

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.sello.wherethereis4cast.data.DataOrException
import com.sello.wherethereis4cast.model.Weather
import com.sello.wherethereis4cast.navigation.state.WeatherBackgroundState
import com.sello.wherethereis4cast.repository.WeatherApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: WeatherApiRepository) : ViewModel() {
    suspend fun fetchWeatherUpdate(city: String = ""): DataOrException<Weather, Boolean, Exception> {
        return repository.getWeatherUpdate(locationOfCity = city)
    }

    suspend fun fetchWeatherUpdate(latitude: Double, longitude: Double): DataOrException<Weather, Boolean, Exception> {
        return repository.getWeatherUpdate(latitude, longitude)
    }

    fun getWeatherConditionBackground(main: String?): Pair<String, String> ? {
        return main?.let { WeatherBackgroundState.getBackgroundValue(it) }
    }
}