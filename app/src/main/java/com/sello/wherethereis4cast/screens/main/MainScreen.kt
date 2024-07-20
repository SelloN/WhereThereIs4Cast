package com.sello.wherethereis4cast.screens.main

import android.content.Context
import android.util.Log
import android.widget.Toast
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
import androidx.compose.material.AlertDialog
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Divider
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.produceState
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.sello.wherethereis4cast.utils.isNetworkAvailable

@Composable
fun MainScreen(
    navController: NavController,
    mainViewModel: MainViewModel = hiltViewModel(),
    latitude: String,
    longitude: String
) {
    val weatherDataState: DataOrException<Weather, Boolean, Exception>
    val context: Context = LocalContext.current
    val isConnected = isNetworkAvailable(context)
    val searchedCity =
        navController.currentBackStackEntry?.savedStateHandle?.get<String>("searchedCity")

    if (!searchedCity.isNullOrEmpty()) {
        weatherDataState = produceState<DataOrException<Weather, Boolean, Exception>>(
            initialValue = DataOrException(loading = true)
        ) {
            value = if (isConnected) {
                mainViewModel.fetchWeatherUpdate(city = searchedCity)
            } else {
                DataOrException(isConnected = false, loading = false)
            }
        }.value
    } else {
        weatherDataState = produceWeatherStateUsingCoordinates(mainViewModel, latitude, longitude)
    }

    WeatherContent(
        weatherDataState, mainViewModel, navController, latitude = latitude,
        longitude = longitude
    )
}

@Composable
fun produceWeatherStateUsingCoordinates(
    mainViewModel: MainViewModel,
    latitude: String,
    longitude: String
): DataOrException<Weather, Boolean, Exception> {
    return produceState<DataOrException<Weather, Boolean, Exception>>(
        initialValue = DataOrException(
            loading = true
        )
    ) {
        value = mainViewModel.fetchWeatherUpdate(latitude.toDouble(), longitude.toDouble())
    }.value
}

@Composable
fun WeatherContent(
    weatherDataState: DataOrException<Weather, Boolean, Exception>,
    mainViewModel: MainViewModel,
    navController: NavController,
    latitude: String = String(),
    longitude: String = String(),
) {
    when {
        weatherDataState.isConnected == false -> {
            Log.d("WeatherContent method",
                "No internet connection. Please check your network settings.")
            ShowOfflineDialog()
        }

        weatherDataState.loading == true -> {
            CircularProgressIndicator()
        }

        weatherDataState.data != null && weatherDataState.isSearchedFromTextFieldLocationFound == false -> {

            val imageBackground: Pair<String, String>? =
                getImageBackground(weatherDataState, mainViewModel)

            Log.d("WeatherContent method", "Searching for lat and long")
            MainScaffold(weatherDataState, navController, imageBackground)
        }

        weatherDataState.isSearchedFromTextFieldLocationFound == true -> {

            val imageBackground: Pair<String, String>? =
                getImageBackground(weatherDataState, mainViewModel)

            Log.d("WeatherContent method", "Searched city: ${weatherDataState.data?.city}")
            MainScaffold(weatherDataState, navController, imageBackground)
        }

        weatherDataState.isSearchedFromTextFieldLocationFound == false &&
                weatherDataState.exception != null -> {

            val producedWeatherState =
                produceWeatherStateUsingCoordinates(mainViewModel, latitude, longitude)

            if (producedWeatherState.data != null) {
                val imageBackground: Pair<String, String>? =
                    getImageBackground(producedWeatherState, mainViewModel)

                Log.d("WeatherContent method", "Searched city: ${weatherDataState.data?.city}")
                MainScaffold(producedWeatherState, navController, imageBackground, true)
            }
        }

    }

    Log.d("WeatherContent: WeatherDataState ", weatherDataState.data.toString())
}

@Composable
fun ShowOfflineDialog() {
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = { Text(text = "Network offline") },
            text = { Text(text = "You can retry after turning it on") },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                    }) { Text("Retry") }
            },
        )
    }
}

fun getImageBackground(
    weatherDataState: DataOrException<Weather, Boolean, Exception>,
    mainViewModel: MainViewModel
): Pair<String, String>? {
    return mainViewModel.getWeatherConditionBackground(
        weatherDataState.data?.list?.get(0)?.weather?.get(0)?.main
    )
}

@Composable
fun MainScaffold(
    weatherState: DataOrException<Weather, Boolean, Exception>,
    navController: NavController,
    imageBackground: Pair<String, String>?,
    showToast: Boolean = false
) {
    val backgroundImageResourcesId = fetchResourceId(imageBackground?.first, "drawable")
    val colorResourceId = fetchResourceId(imageBackground?.second, "color")

    Box {
        Image(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.4f),
            painter = painterResource(backgroundImageResourcesId),
            contentDescription = "background_image",
            contentScale = ContentScale.FillBounds
        )

        Scaffold(
            backgroundColor = Color.Transparent,
            topBar = {
                WeatherTopBar(
                    title = weatherState.data?.city?.name + " ,${weatherState.data?.city?.country}",
                    navController = navController,
                    onAddActionClicked = {
                        navController.navigate(WeatherScreens.SearchScreen.name)
                    }) {
                    Log.d("BTN", "MainScaffold: Button clicked")
                }
            },
            modifier = Modifier.padding(all = 1.dp),
        ) { innerPadding ->
            MainContent(innerPadding, weatherState.data!!, colorResourceId, showToast)
        }
    }
}

@Composable
fun MainContent(
    innerPadding: PaddingValues,
    data: Weather,
    colorResourceId: Int,
    showToast: Boolean,
) {
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
            WeatherForToday(weatherItem)
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(1f)
                .background(colorResource(id = colorResourceId))
        ) {
            Column {
                TemperaturePercentiles(weatherItem)
                Divider(color = Color.White)
                WeatherTemperatureDays(data)
            }
        }
        if(showToast) ShowToast(LocalContext.current, message = "Your searched location wasn't found." +
                " We've reverted to your current location. Please try again.")
    }
}

@Composable
fun WeatherForToday(weatherItem: WeatherItem) {
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
fun WeatherTemperatureDays(data: Weather) {

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
fun TemperaturePercentiles(weatherItem: WeatherItem) {
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

@Composable
fun ShowToast(context: Context, message: String) {
    LaunchedEffect(message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}


