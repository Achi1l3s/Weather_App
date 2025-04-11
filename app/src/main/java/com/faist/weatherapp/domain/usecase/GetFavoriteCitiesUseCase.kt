package com.faist.weatherapp.domain.usecase

import com.faist.weatherapp.domain.repository.FavoriteRepository
import javax.inject.Inject

class GetFavoriteCitiesUseCase @Inject constructor(
    private val repository: FavoriteRepository
) {

    operator fun invoke() = repository.favoriteCities
}