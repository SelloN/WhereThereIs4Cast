package com.sello.wherethereis4cast.screens.main

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
                .fillMaxHeight(0.342f),
            contentAlignment = Alignment.Center
        ) {
            Column(modifier = Modifier.padding(50.dp)) {
                Text(text = "25°", style = TextStyle(fontSize = 70.sp, color = Color.White))
                Text(text = "SUNNY", style = TextStyle(fontSize = 30.sp, color = Color.White))
            }
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f)
                .background(
                    colorResource(id = R.color.sea_sunny_blue)
                )
        ) {
            Column {
                DisplayTemperatureCounts()
                Divider(color = Color.White)
                DisplayWeatherTemperatureDays()
            }
        }
    }
}

@Composable
fun DisplayWeatherTemperatureDays() {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        Row(
            modifier = Modifier
                .padding(horizontal = 30.dp, vertical = 20.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Text(text = "Monday°", style = TextStyle(fontSize = 20.sp, color = Color.White))
            Image(painter = painterResource(id = R.drawable.sunny_24), contentDescription = "clear icon")
            Text(text = "19°", style = TextStyle(fontSize = 15.sp, color = Color.White))
        }
    }
}

@Composable
fun DisplayTemperatureCounts() {
    Row(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = "19°", style = TextStyle(fontSize = 20.sp, color = Color.White))
            Text(text = "min", style = TextStyle(fontSize = 15.sp, color = Color.White))
        }
        Column {
            Text(text = "25°", style = TextStyle(fontSize = 20.sp, color = Color.White))
            Text(text = "current", style = TextStyle(fontSize = 15.sp, color = Color.White))
        }
        Column {
            Text(text = "27°", style = TextStyle(fontSize = 20.sp, color = Color.White))
            Text(text = "max", style = TextStyle(fontSize = 15.sp, color = Color.White))
        }
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
