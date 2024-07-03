package com.sello.wherethereis4cast.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.sello.wherethereis4cast.model.Favourite


@Database(entities = [Favourite::class], version = 1, exportSchema = false)
abstract class WeatherDatabase: RoomDatabase(){
    abstract fun weatherDao(): WeatherDao
}