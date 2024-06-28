package com.sello.wherethereis4cast.screens.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.rounded.MoreVert
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun WeatherTopBar(
    title: String = "Title",
    icon: ImageVector? = null,
    isMainScreen: Boolean = true,
    elevation: Dp = 3.dp,
//    navController: NavController,
    onAddActionClicked: () -> Unit = {},
    onButtonClicked: () -> Unit = {},
) {
    TopAppBar(title = {
        Text(
            text = title,
            color = MaterialTheme.colors.onSecondary,
            style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 15.sp)
        )
    }, actions = {
        if (isMainScreen) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Default.Search, contentDescription = "search icon")
            }
            IconButton(onClick = { /*TODO*/ }) {
                Icon(imageVector = Icons.Rounded.MoreVert, contentDescription = "more icon")
            }
        } else
            Box {}

    }, navigationIcon = {
        if (icon != null) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = MaterialTheme.colors.onSecondary,
                modifier = Modifier.clickable {
                    onButtonClicked.invoke()
                },
            )
        }
    },
        backgroundColor = Color.Transparent,
        elevation = elevation,
        modifier = Modifier
            .clip(
                shape = RoundedCornerShape(
                    bottomEnd = 5.dp,
                    bottomStart = 5.dp
                )
            ),
    )
}
