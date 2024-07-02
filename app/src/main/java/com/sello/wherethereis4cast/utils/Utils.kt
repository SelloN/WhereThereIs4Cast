package com.sello.wherethereis4cast.utils

import android.content.Context
import android.content.res.Resources
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat

fun formatDate(timestamp: Int): String {
    val sdf = SimpleDateFormat("EEE, MMM d")
    val date = java.util.Date(timestamp.toLong() * 1000)

    return sdf.format(date)
}

fun formatDecimals(item: Double): String {
    return " %.0f".format(item)
}

@Composable
fun fetchResourceId(name: String?, defType: String): Int  {

    val context: Context = LocalContext.current
    val resources: Resources = context.resources
    val resourcesId =
        resources.getIdentifier(name,
            defType, context.packageName)

    return resourcesId
}