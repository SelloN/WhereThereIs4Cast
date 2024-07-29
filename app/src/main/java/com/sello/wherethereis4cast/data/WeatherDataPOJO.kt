package com.sello.wherethereis4cast.data

data class WeatherDataPOJO<T>(
    var data: T? = null,
    var loading: Boolean? = null,
    var hasException: Boolean? = null,
    var isConnected: Boolean? = null,
    var isSearchedFromTextFieldLocationFound: Boolean? = null
)