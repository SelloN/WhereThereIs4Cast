package com.sello.wherethereis4cast.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.sello.wherethereis4cast.navigation.state.WeatherScreens
import com.sello.wherethereis4cast.screens.main.MainScreen
import com.sello.wherethereis4cast.screens.main.MainViewModel
import com.sello.wherethereis4cast.screens.search.SearchScreen
import com.sello.wherethereis4cast.screens.splashscreen.SplashScreen
import com.sello.wherethereis4cast.screens.about.AboutScreen
import com.sello.wherethereis4cast.screens.favourites.FavouritesScreen
import com.sello.wherethereis4cast.screens.search.SearchedLocationViewModel
import com.sello.wherethereis4cast.screens.settings.SettingsScreen

@ExperimentalComposeUiApi
@Composable
fun WeatherNavigation() {

    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = WeatherScreens.SplashScreen.name) {
        composable(WeatherScreens.SplashScreen.name){
            SplashScreen(navController = navController)
        }

        val route = WeatherScreens.MainScreen.name
        composable("$route/{textLat}/{textLong}",
            arguments = listOf(
                navArgument(name = "textLat") {type = NavType.StringType },
                navArgument(name= "textLong") {type = NavType.StringType })
            ){ backStackEntry ->

            val textLat = backStackEntry.arguments!!.getString("textLat")
            val textLong = backStackEntry.arguments!!.getString("textLong")

            val mainViewModel = hiltViewModel<MainViewModel>()

            MainScreen(navController = navController, mainViewModel, latitude = textLat.toString(),
                longitude = textLong.toString())
        }

        composable(WeatherScreens.SearchScreen.name){
            SearchScreen(navController = navController)
        }

        composable(WeatherScreens.AboutScreen.name){
            AboutScreen(navController = navController)
        }

        composable(WeatherScreens.SettingsScreen.name){
            SettingsScreen(navController = navController)
        }

        composable(WeatherScreens.FavouriteScreen.name){
            FavouritesScreen(navController = navController)
        }
    }
}
