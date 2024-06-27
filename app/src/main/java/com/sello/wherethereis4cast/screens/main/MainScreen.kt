package com.sello.wherethereis4cast.screens.main

import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sello.wherethereis4cast.data.DataOrException
import com.sello.wherethereis4cast.model.Weather

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel()
) {
    val weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
        initialValue = DataOrException(loading = true)
    ) {
        value = mainViewModel.fetchWeatherUpdate(city = "Johannesburg")
    }.value

    if (weatherData.loading == true) {
        CircularProgressIndicator()
    } else if (weatherData.data != null)
        MainScaffold(weather = weatherData.data!!, navController)
}

@Composable
fun MainContent(data: Weather) {
    Text(text = "Weather ${data.city}")
}

@Composable
fun MainScaffold(weather: Weather, navController: NavController){
    Scaffold(topBar = {}){
        MainContent(data = weather)
    }
}
