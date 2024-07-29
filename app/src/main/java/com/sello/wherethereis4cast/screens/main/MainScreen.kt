package com.sello.wherethereis4cast.screens.main

import android.app.Activity
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
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
import coil.compose.rememberAsyncImagePainter
import com.sello.wherethereis4cast.data.WeatherDataPOJO
import com.sello.wherethereis4cast.model.City
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
    var weatherDataState = mainViewModel.weatherDataState.collectAsState().value
    val isConnected = isNetworkAvailable(LocalContext.current)
    val searchedCity =
        navController.currentBackStackEntry?.savedStateHandle?.get<String>("searchedCity")

    if (isConnected) {
        if (!searchedCity.isNullOrEmpty()) {
            mainViewModel.fetchWeatherUpdate(city = searchedCity)
        } else {
            mainViewModel.fetchWeatherUpdate(latitude.toDouble(), longitude.toDouble())
        }
    } else
        weatherDataState = WeatherDataPOJO(isConnected = false, loading = false)

    WeatherContent(
        weatherDataState, mainViewModel, navController
    )
}

@Composable
fun WeatherContent(
    weatherDataState: WeatherDataPOJO<Weather, Exception>,
    mainViewModel: MainViewModel,
    navController: NavController
) {
    when {
        weatherDataState.isConnected == false -> {
            Log.d(
                "WeatherContent",
                "No internet connection. Please check your network settings."
            )
            OfflineDialog()
        }

        weatherDataState.loading == true -> {
            CircularProgressIndicator()
        }

        weatherDataState.isSearchedFromTextFieldLocationFound == true -> {
            val imageBackground: Pair<String, String>? =
                getImageBackground(weatherDataState, mainViewModel)

            Log.d("WeatherContent", "Searched city: ${weatherDataState.data?.city}")
            MainScaffold(weatherDataState, navController, imageBackground)
        }

        weatherDataState.isSearchedFromTextFieldLocationFound == false
                && weatherDataState.exception != null -> {
            val imageBackground: Pair<String, String>? =
                getImageBackground(weatherDataState, mainViewModel)

            Log.d(
                "WeatherContent",
                "Searched location wasn't found: ${weatherDataState.data?.city}"
            )

            mainViewModel.clear()
            MainScaffold(weatherDataState, navController, imageBackground, showToast = true)
        }

        weatherDataState.data != null && weatherDataState.exception == null -> {

            val imageBackground: Pair<String, String>? =
                getImageBackground(weatherDataState, mainViewModel)

            Log.d(
                "WeatherContent",
                "Location found through lat and long co-ord: ${weatherDataState.data?.city}"
            )
            MainScaffold(weatherDataState, navController, imageBackground)
        }

    }

    Log.d("WeatherContent: WeatherDataState ", weatherDataState.data.toString())
}

@Composable
fun OfflineDialog() {
    val openDialog = remember { mutableStateOf(true) }

    if (openDialog.value) {
        val context = LocalContext.current

        AlertDialog(
            onDismissRequest = {
                openDialog.value = false
            },
            title = { Text(text = "Network offline") },
            text = { Text(text = "Please switch on your network and restart app") },
            confirmButton = {
                TextButton(
                    onClick = {
                        val activity = context as? Activity
                        openDialog.value = false
                        activity?.finish()

                    }) { Text("Close App") }
            },
        )
    }
}

fun getImageBackground(
    weatherDataState: WeatherDataPOJO<Weather, Exception>,
    mainViewModel: MainViewModel
): Pair<String, String>? {
    return mainViewModel.getWeatherConditionBackground(
        weatherDataState.data?.list?.get(0)?.weather?.get(0)?.main
    )
}

@Composable
fun MainScaffold(
    weatherDataState: WeatherDataPOJO<Weather, Exception>,
    navController: NavController,
    imageBackground: Pair<String, String>?,
    showToast: Boolean = false,
) {
    val backgroundImageResourcesId = fetchResourceId(imageBackground?.first, "drawable")
    val colorResourceId = fetchResourceId(imageBackground?.second, "color")
    val weatherData = weatherDataState.data
    val city: City? = weatherData?.city

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
                    title = city?.name + ",${city?.country}",
                    navController = navController,
                    onAddActionClicked = {
                        navController.navigate(WeatherScreens.SearchScreen.name)
                    },
                    latitude = city?.coord?.lat.toString(),
                    longitude = city?.coord?.lon.toString()
                ) {
                    Log.d("BTN", "MainScaffold: Button clicked")
                }
            },
            modifier = Modifier.padding(all = 1.dp),
        ) { innerPadding ->
            MainContent(innerPadding, weatherDataState.data!!, colorResourceId, showToast)
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
        if (showToast) Toast(
            LocalContext.current, message = "Your searched location wasn't found."
        )
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
        painter = rememberAsyncImagePainter(imageUrl),
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
fun Toast(context: Context, message: String) {
    LaunchedEffect(message) {
        Toast.makeText(context, message, Toast.LENGTH_LONG).show()
    }
}



