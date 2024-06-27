package com.sello.wherethereis4cast.network

import com.sello.wherethereis4cast.model.Weather
import com.sello.wherethereis4cast.utils.Constants
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

@Singleton
interface WeatherAPI {
    @GET(value = "data/2.5/forecast/daily")
    suspend fun getWeatherUpdate(
        @Query(value = "q") location: String,
        @Query(value = "appid") apiId: String = Constants.API_KEY,
        @Query(value = "units") units: String = Constants.METRIC,
        ): Weather
}