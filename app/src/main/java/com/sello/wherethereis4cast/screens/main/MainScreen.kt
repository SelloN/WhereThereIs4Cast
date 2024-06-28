package com.sello.wherethereis4cast.screens.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.sello.wherethereis4cast.R

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel()
) {
//    val weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
//        initialValue = DataOrException(loading = true)
//    ) {
//        value = mainViewModel.fetchWeatherUpdate(city = "Johannesburg")
//    }.value
//
//    if (weatherData.loading == true) {
//        CircularProgressIndicator()
//    } else if (weatherData.data != null)
//        MainScaffold(weather = weatherData.data!!, navController)
    MainScaffold()
}

@Composable
fun MainContent(innerPadding: PaddingValues) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize(),
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.34f)
        )
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f)
                .background(colorResource(id = R.color.sea_sunny_blue))
        )
    }
}

@Preview
@Composable
fun MainScaffold() {
    Box {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f),
            painter = painterResource(R.drawable.sea_sunnypng),
            contentDescription = "background_image",
            contentScale = ContentScale.FillBounds
        )

        Scaffold(
            backgroundColor = Color.Transparent,
            topBar = {
                WeatherTopBar(title = "Johannesburg") {
                    Log.d("BTN", "MainScaffold: Button clicked")
                }
            },
            modifier = Modifier.padding(all = 1.dp)
        ) { innerPadding ->
            MainContent(innerPadding)
        }
    }
}
