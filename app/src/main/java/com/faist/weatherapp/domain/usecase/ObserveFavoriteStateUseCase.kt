package com.faist.weatherapp.domain.usecase

import com.faist.weatherapp.domain.repository.FavoriteRepository
import javax.inject.Inject

class ObserveFavoriteStateUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {

    operator fun invoke(cityId: Int) = repository.observeIsFavorite(cityId)
}