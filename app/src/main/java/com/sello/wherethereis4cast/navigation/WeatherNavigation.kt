package com.sello.wherethereis4cast.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.sello.wherethereis4cast.screens.main.MainScreen
import com.sello.wherethereis4cast.screens.main.MainViewModel
import androidx.hilt.navigation.compose.hiltViewModel


@Composable
fun WeatherNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = WeatherScreens.MainScreen.name){
        composable(WeatherScreens.MainScreen.name){
            val mainViewModel: MainViewModel = hiltViewModel<MainViewModel>()
            MainScreen(navController = navController, mainViewModel)
        }
    }
}
