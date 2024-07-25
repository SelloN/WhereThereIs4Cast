package com.sello.wherethereis4cast.screens.favourites

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sello.wherethereis4cast.model.Favourite
import com.sello.wherethereis4cast.repository.WeatherDbRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavouriteViewModel @Inject constructor(private val repository: WeatherDbRepository) :
    ViewModel() {
    private val _favouriteList = MutableStateFlow<List<Favourite>>(emptyList())
    val favouriteList = _favouriteList.asStateFlow()

    init {
        viewModelScope.launch {
            repository.getFavourites().distinctUntilChanged()
                .collect { listOfFavourites ->
                    _favouriteList.value = listOfFavourites
                    Log.d("Favourites", ":${favouriteList.value} ")
                }
        }
    }

    fun insertFavourite(favourite: Favourite) =
        viewModelScope.launch { repository.insertFavourite(favourite) }

    fun updateFavourite(favourite: Favourite) =
        viewModelScope.launch { repository.updateFavourite(favourite) }

    fun deleteFavorite(favourite: Favourite) =
        viewModelScope.launch { repository.deleteFavourite(favourite) }
}