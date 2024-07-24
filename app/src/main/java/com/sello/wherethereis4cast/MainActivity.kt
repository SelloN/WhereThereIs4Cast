package com.sello.wherethereis4cast

import android.Manifest
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.AlertDialog
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import com.sello.wherethereis4cast.navigation.WeatherNavigation
import com.sello.wherethereis4cast.navigation.state.LocationPermissionState
import com.sello.wherethereis4cast.ui.theme.WhereThereIs4CastTheme
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val locationPermissions = arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )

            var locationPermissionsGranted by remember {
                mutableStateOf(areLocationPermissionsAlreadyGranted())
            }

            var shouldShowPermissionRationale by remember {
                mutableStateOf(shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION))
            }

            var currentPermissionsStatus by remember {
                mutableStateOf(
                    decideCurrentPermissionStatus(
                        locationPermissionsGranted,
                        shouldShowPermissionRationale
                    )
                )
            }

            val locationPermissionLauncher = rememberLauncherForActivityResult(
                contract = ActivityResultContracts.RequestMultiplePermissions(),
                onResult = { permissions ->
                    locationPermissionsGranted =
                        permissions.values.reduce { acc, isPermissionGranted ->
                            acc && isPermissionGranted
                        }

                    if (!locationPermissionsGranted) {
                        shouldShowPermissionRationale =
                            shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_COARSE_LOCATION)
                    }

                    currentPermissionsStatus = decideCurrentPermissionStatus(
                        locationPermissionsGranted,
                        shouldShowPermissionRationale
                    )
                })

            val lifecycleOwner = LocalLifecycleOwner.current
            DisposableEffect(key1 = lifecycleOwner, effect = {
                val observer = LifecycleEventObserver { _, event ->
                    if (event == Lifecycle.Event.ON_START &&
                        !locationPermissionsGranted &&
                        !shouldShowPermissionRationale
                    ) {
                        locationPermissionLauncher.launch(locationPermissions)
                    }
                }
                lifecycleOwner.lifecycle.addObserver(observer)
                onDispose {
                    lifecycleOwner.lifecycle.removeObserver(observer)
                }
            })

            val scope = rememberCoroutineScope()

            Scaffold { _ ->
                if (shouldShowPermissionRationale) {
                    AlertDialog(
                        title = { Text("Permissions") },
                        text = { Text("You need to accept permissions to use this app") },
                        onDismissRequest = {},
                        confirmButton = {
                            Button(onClick = {
                                scope.launch {
                                    shouldShowPermissionRationale = false
                                    locationPermissionLauncher.launch(locationPermissions)
                                }
                            }) {
                                Text(text = "Approve")
                            }
                        },
                        dismissButton = {
                            Button(onClick = {
                                shouldShowPermissionRationale = false
                                this.finish()
                            }) {
                                Text(text = "Close App")
                            }
                        }
                    )
                }
            }

            if (currentPermissionsStatus == LocationPermissionState.Granted.name) {
                WeatherForeCastApp()
            }
        }
    }

    private fun areLocationPermissionsAlreadyGranted(): Boolean {
        return ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
    }

    private fun decideCurrentPermissionStatus(
        locationPermissionsGranted: Boolean,
        shouldShowPermissionRationale: Boolean
    ): String {
        return if (locationPermissionsGranted) LocationPermissionState.Granted.name
        else if (shouldShowPermissionRationale) LocationPermissionState.Rejected.name
        else LocationPermissionState.Denied.name
    }
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun WeatherForeCastApp() {
    WhereThereIs4CastTheme {
        Surface(color = MaterialTheme.colors.background, modifier = Modifier.fillMaxSize()) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                WeatherNavigation()
            }
        }
    }
}