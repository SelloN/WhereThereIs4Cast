package com.sello.wherethereis4cast.screens.main

import androidx.lifecycle.ViewModel
import com.sello.wherethereis4cast.data.DataOrException
import com.sello.wherethereis4cast.model.Weather
import com.sello.wherethereis4cast.repository.WeatherRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val repository: WeatherRepository) : ViewModel() {

    suspend fun fetchWeatherUpdate(city: String) : DataOrException<Weather, Boolean, Exception> {
        return repository.getWeatherUpdate(locationOfCity = city)
    }

//    val weather: MutableState<DataOrException<Weather, Boolean, Exception>> = mutableStateOf(
//        DataOrException(null, true, Exception("")))
//
//    private fun getWeather() {
//        viewModelScope.launch {
//            if(city.isEmpty()) return@launch
//            weather.value.loading = true
//            weather.value = repository.getWeatherUpdate(city)
//            if(weather.value.data.toString().isEmpty()) weather.value.loading = false
//
//        }
//
//        Log.d("GET", "getWeather: " + weather.value)
//    }
}