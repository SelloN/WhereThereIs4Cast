package com.sello.wherethereis4cast.screens.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sello.wherethereis4cast.data.WeatherDataPOJO
import com.sello.wherethereis4cast.model.Weather
import com.sello.wherethereis4cast.navigation.state.WeatherBackgroundState
import com.sello.wherethereis4cast.repository.WeatherApiRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: WeatherApiRepository) :
    ViewModel() {

    private val _weatherDataState =
        MutableStateFlow<WeatherDataPOJO<Weather>>(WeatherDataPOJO(loading = true))
    val weatherDataState = _weatherDataState.asStateFlow()

    fun fetchWeatherUpdate(city: String = "") = viewModelScope.launch {
        val currentState = _weatherDataState.value
        val weatherData = repository.getWeatherUpdate(locationOfCity = city, currentState)
        _weatherDataState.value = weatherData
    }

    fun fetchWeatherUpdate(latitude: Double, longitude: Double) = viewModelScope.launch {
        val currentState = _weatherDataState.value
        val weatherData =
            repository.getWeatherUpdate(latitude = latitude, longitude = longitude, currentState)
        _weatherDataState.value = weatherData
    }

    fun getWeatherConditionBackground(main: String?): Pair<String, String>? {
        return main?.let { WeatherBackgroundState.getBackgroundValue(it) }
    }

    fun clearException() {
        _weatherDataState.value = _weatherDataState.value.copy(
            hasException = null,
            loading = false,
            isSearchedFromTextFieldLocationFound = false
        )
    }
}