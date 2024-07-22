package com.sello.wherethereis4cast.data

import com.sello.wherethereis4cast.model.City
import com.sello.wherethereis4cast.model.Coord
import com.sello.wherethereis4cast.model.FeelsLike
import com.sello.wherethereis4cast.model.Temp
import com.sello.wherethereis4cast.model.Weather
import com.sello.wherethereis4cast.model.WeatherItem
import com.sello.wherethereis4cast.model.WeatherObject

class FakeWeatherApiRepositoryData{

    fun getWeatherUpdate(
        cityName: String = "Johannesburg",
        latitude: Double = -26.2023,
        longitude: Double = 28.0436) : DataOrException<Weather, Boolean, Exception> {

        val city = City(
            id = 993800,
            coord = Coord(lat = latitude, lon = longitude),
            country = "ZA",
            name = cityName,
            population = 2026469,
            timezone = 7200
        )

        val weatherObject = WeatherObject(
            id = 800,
            main = "Clear",
            description = "sky is clear",
            icon = "01d"
        )

        val weatherItem1 = WeatherItem(
            dt = 1721642400,
            sunrise = 1721623910,
            sunset = 1721662583,
            temp = Temp(
                day = 15.51,
                min = 9.92,
                max = 19.24,
                night = 13.52,
                eve = 17.18,
                morn = 10.73
            ),
            feels_like = FeelsLike(
                day = 14.29,
                night = 11.84,
                eve = 15.74,
                morn = 8.49
            ),
            pressure = 1022,
            humidity = 45,
            speed = 5.05,
            deg = 304,
            gust = 9.69,
            clouds = 2,
            pop = 5.05,
            rain = 5.05,
            weather = listOf(weatherObject)
        )

        val weatherItem2 = WeatherItem(
            dt = 1721642400,
            sunrise = 1721623910,
            sunset = 1721662583,
            temp = Temp(
                day = 15.51,
                min = 9.92,
                max = 19.24,
                night = 13.52,
                eve = 17.18,
                morn = 10.73
            ),
            feels_like = FeelsLike(
                day = 14.29,
                night = 11.84,
                eve = 15.74,
                morn = 8.49
            ),
            pressure = 1022,
            humidity = 45,
            speed = 5.05,
            deg = 304,
            gust = 9.69,
            clouds = 2,
            pop = 5.05,
            rain = 5.05,
            weather = listOf(weatherObject)
        )

        val weather = Weather(
            city = city,
            cod = "200",
            message = 6.6668546,
            cnt = 7,
            list = listOf(weatherItem1, weatherItem2)
        )

        return DataOrException(data = weather, isSearchedFromTextFieldLocationFound = true)
    }
}