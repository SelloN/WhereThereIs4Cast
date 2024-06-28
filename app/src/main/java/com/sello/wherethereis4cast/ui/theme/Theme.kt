package com.sello.wherethereis4cast.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable

@Composable
fun WhereThereIs4CastTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        typography = Typography,
        content = content
    )
}