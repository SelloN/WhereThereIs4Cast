package com.sello.wherethereis4cast.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sello.wherethereis4cast.model.Favourite
import com.sello.wherethereis4cast.model.SearchedLocation


@Database(entities = [Favourite::class, SearchedLocation::class], version = 2, exportSchema = false)
abstract class WeatherDatabase: RoomDatabase(){
    abstract fun weatherDao(): WeatherDao
}