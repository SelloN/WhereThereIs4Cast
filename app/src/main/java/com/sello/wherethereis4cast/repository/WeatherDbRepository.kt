package com.sello.wherethereis4cast.repository

import com.sello.wherethereis4cast.data.WeatherDao
import com.sello.wherethereis4cast.model.Favourite
import com.sello.wherethereis4cast.model.SearchedLocation
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class WeatherDbRepository @Inject constructor(private val weatherDao: WeatherDao) {
    fun getFavourites(): Flow<List<Favourite>> = weatherDao.getFavourites()
    fun getSearchedLocations(): Flow<List<SearchedLocation>> = weatherDao.getSearchedLocations()

    suspend fun insertFavourite(favourite: Favourite) = weatherDao.insertFavourite(favourite)
    suspend fun updateFavourite(favourite: Favourite) = weatherDao.updateFavourite(favourite)
    suspend fun deleteFavourite(favourite: Favourite) = weatherDao.deleteFavourite(favourite)

    suspend fun getSearchedLocationById(city: String): SearchedLocation =
        weatherDao.getSearchedLocation(city)

    suspend fun insertSearchedLocation(searchedLocation: SearchedLocation) =
        weatherDao.insertSearchedLocation(searchedLocation)

    suspend fun deleteAllSearchedLocations() = weatherDao.deleteAllSearchedLocations()
}