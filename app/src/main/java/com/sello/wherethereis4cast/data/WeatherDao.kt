package com.sello.wherethereis4cast.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.sello.wherethereis4cast.model.Favourite
import com.sello.wherethereis4cast.model.SearchedLocation
import kotlinx.coroutines.flow.Flow

@Dao
interface WeatherDao {
    //Favourite
    @Query("SELECT * from favourite")
    fun getFavourites(): Flow<List<Favourite>>

    @Query("SELECT * from favourite where city =:city")
    suspend fun getFavById(city: String): Favourite

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFavourite(favourite: Favourite)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    suspend fun updateFavourite(favourite: Favourite)

    @Query("DELETE from favourite")
    suspend fun deleteAllFavourites()

    @Delete
    suspend fun deleteFavourite(favourite: Favourite)

    //SearchedLocation
    @Query("SELECT * from searchedLocation where city =:city")
    suspend fun getSearchedLocation(city: String): SearchedLocation

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSearchedLocation(searchedLocation: SearchedLocation)

    @Delete
    suspend fun deleteSearchedLocation(searchedLocation: SearchedLocation)

    @Query("SELECT * from searchedLocation")
    fun getSearchedLocations(): Flow<List<SearchedLocation>>

    @Query("DELETE from searchedLocation")
    suspend fun deleteAllSearchedLocations()
}