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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.produceState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.sello.wherethereis4cast.data.DataOrException
import com.sello.wherethereis4cast.model.Weather
import com.sello.wherethereis4cast.model.WeatherItem
import com.sello.wherethereis4cast.navigation.state.WeatherScreens
import com.sello.wherethereis4cast.utils.fetchResourceId
import com.sello.wherethereis4cast.utils.formatDate
import com.sello.wherethereis4cast.utils.formatDecimals

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel(),
    latitude: String, longitude: String, city: String = "") {

    val weatherData: Any

    if (latitude.toDouble() != 0.0) {
        weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
            initialValue = DataOrException(loading = true)
        ) {
            value = mainViewModel.fetchWeatherUpdate(latitude.toDouble(), longitude.toDouble())
        }.value
    } else {
        weatherData = produceState<DataOrException<Weather, Boolean, Exception>>(
            initialValue = DataOrException(loading = true)
        ) {
            value = mainViewModel.fetchWeatherUpdate(city = city)
        }.value
    }

    if (weatherData.loading == true) {
        CircularProgressIndicator()
    } else {
        val imageBackground: Pair<String, String>? =
            mainViewModel.getWeatherConditionBackground(
                weatherData.data?.list?.get(0)?.weather?.get(0)?.main)

        MainScaffold(data = weatherData.data!!, navController,
            fetchResourceId(imageBackground?.first, "drawable"),
            fetchResourceId(imageBackground?.second, "color"))
    }

}

@Composable
fun MainContent(innerPadding: PaddingValues, data: Weather, colorResourceId: Int) {
    val weatherItem = data.list[0]

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding),
    ) {

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.342f),
            contentAlignment = Alignment.Center
        ) {
            DisplayWeatherForToday(weatherItem)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f)
                .background(colorResource(id = colorResourceId))
        ) {
            Column {
                DisplayTemperaturePercentiles(weatherItem)
                Divider(color = Color.White)
                DisplayWeatherTemperatureDays(data)
            }
        }
    }
}

@Composable
fun DisplayWeatherForToday(weatherItem: WeatherItem) {
    Column(modifier = Modifier.padding(50.dp)) {
        Text(
            text = "${formatDecimals(weatherItem.temp.day)}°",
            style = TextStyle(fontSize = 70.sp, color = Color.White)
        )
        Text(
            text = weatherItem.weather[0].main.uppercase(),
            style = TextStyle(fontSize = 30.sp, color = Color.White)
        )
    }
}

@Composable
fun MainScaffold(
    data: Weather,
    navController: NavController,
    resourcesId: Int,
    colorResourceId: Int
) {

    Box {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f),
            painter = painterResource(resourcesId),
            contentDescription = "background_image",
            contentScale = ContentScale.FillBounds
        )

        Scaffold(
            backgroundColor = Color.Transparent, topBar = {
                WeatherTopBar(
                    title = data.city.name + " ,${data.city.country}",
                    navController = navController,
                    onAddActionClicked = {
                    navController.navigate(WeatherScreens.SearchScreen.name)
                }) {
                    Log.d("BTN", "MainScaffold: Button clicked")
                }
            }, modifier = Modifier.padding(all = 1.dp)
        ) { innerPadding ->
            MainContent(innerPadding, data, colorResourceId)
        }
    }
}

@Composable
fun DisplayWeatherTemperatureDays(data: Weather) {

    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxSize()
    ) {
        items(items = data.list) { item: WeatherItem ->
            WeatherDetailRow(item)
        }
    }
}

@Composable
fun WeatherDetailRow(weatherItem: WeatherItem) {
    val imageUrl = "https://openweathermap.org/img/wn/${weatherItem.weather[0].icon}.png"

    Row(
        modifier = Modifier
            .padding(horizontal = 30.dp, vertical = 5.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween,
    ) {
        Text(
            text = formatDate(weatherItem.dt).split(",")[0],
            style = TextStyle(fontSize = 20.sp, color = Color.White),
            modifier = Modifier.weight(0.3f)
        )

        Row(
            modifier = Modifier.weight(0.7f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically,
        ) {
            WeatherStateImage(imageUrl)
            Text(
                text = "${formatDecimals(weatherItem.temp.day)}°",
                style = TextStyle(fontSize = 20.sp, color = Color.White),
                modifier = Modifier
            )
        }
    }
}

@Composable
fun WeatherStateImage(imageUrl: String) {
    Image(
        modifier = Modifier
            .padding(start = 45.dp)
            .size(40.dp),
        painter = rememberImagePainter(imageUrl),
        contentDescription = "icon image"
    )
}

@Composable
fun DisplayTemperaturePercentiles(weatherItem: WeatherItem) {
    Row(
        modifier = Modifier
            .padding(30.dp)
            .fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(
                text = "${formatDecimals(weatherItem.temp.min)}°",
                style = TextStyle(fontSize = 20.sp, color = Color.White)
            )
            Text(text = "min", style = TextStyle(fontSize = 15.sp, color = Color.White))
        }
        Column {
            Text(
                text = "${formatDecimals(weatherItem.temp.day)}°",
                style = TextStyle(fontSize = 20.sp, color = Color.White)
            )
            Text(text = "current", style = TextStyle(fontSize = 15.sp, color = Color.White))
        }
        Column {
            Text(
                text = "${formatDecimals(weatherItem.temp.max)}°",
                style = TextStyle(fontSize = 20.sp, color = Color.White)
            )
            Text(text = "max", style = TextStyle(fontSize = 15.sp, color = Color.White))
        }
    }
}


