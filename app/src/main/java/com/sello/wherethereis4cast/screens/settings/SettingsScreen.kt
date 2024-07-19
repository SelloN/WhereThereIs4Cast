package com.sello.wherethereis4cast.screens.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.sello.wherethereis4cast.screens.main.WeatherTopBar

@Composable
fun SettingsScreen(navController: NavController) {
    Scaffold(topBar = {
        WeatherTopBar(
            title = "Settings Screen",
            icon = Icons.Default.ArrowBack,
            false,
            navController = navController
        ) { navController.popBackStack() }
    }) { contentPadding ->
        contentPadding
        Surface(
            modifier = Modifier
                .padding(5.dp)
                .fillMaxWidth()
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(text = "This is a Settings screen")
            }
        }
    }
}