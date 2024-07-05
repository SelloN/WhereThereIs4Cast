package com.sello.wherethereis4cast.screens.search;

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.sello.wherethereis4cast.model.SearchedLocation
import com.sello.wherethereis4cast.repository.WeatherDbRepository;

import javax.inject.Inject;

import dagger.hilt.android.lifecycle.HiltViewModel;
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

@HiltViewModel
class SearchedLocationViewModel @Inject constructor(private val repository: WeatherDbRepository) :
    ViewModel() {

    private val _searchedLocationList = MutableStateFlow<List<SearchedLocation>>(emptyList())
    val searchedLocation = _searchedLocationList.asStateFlow()

    init {
        viewModelScope.launch(Dispatchers.IO) {
            repository.getSearchedLocations().distinctUntilChanged()
                .collect { searchedLocations ->
                    if (searchedLocations.isEmpty()) {
                        Log.d("TAG", ": Empty favourites ")
                    } else {
                        _searchedLocationList.value = searchedLocations
                        Log.d("FAV", ":${searchedLocations.last().city} ")
                    }
                }
        }
    }

    fun insertSearchedLocation(searchedLocation: SearchedLocation) =
        viewModelScope.launch { repository.insertSearchedLocation(searchedLocation) }

    fun deleteAllSearchedLocations() =
        viewModelScope.launch { repository.deleteAllSearchedLocations() }
}
