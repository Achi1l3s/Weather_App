package com.faist.weatherapp.presentation.favorite

import com.faist.weatherapp.domain.entity.City
import kotlinx.coroutines.flow.StateFlow

interface FavoriteComponent {

    val model: StateFlow<FavoriteStore.State>

    fun onClickSearch()

    fun onClickAddFavorite()

    fun onCityItemClick(city: City)
}