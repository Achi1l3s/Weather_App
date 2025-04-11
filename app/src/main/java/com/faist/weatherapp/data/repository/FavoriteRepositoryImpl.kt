package com.faist.weatherapp.data.repository

import com.faist.weatherapp.data.local.db.FavoriteCitiesDao
import com.faist.weatherapp.data.mapper.toDbModel
import com.faist.weatherapp.data.mapper.toEntities
import com.faist.weatherapp.domain.entity.City
import com.faist.weatherapp.domain.repository.FavoriteRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class FavoriteRepositoryImpl @Inject constructor(
    private val favoriteCitiesDao: FavoriteCitiesDao
) : FavoriteRepository {

    override val favoriteCities: Flow<List<City>> = favoriteCitiesDao.getFavoriteCities()
        .map { it.toEntities() }

    override fun observeIsFavorite(cityId: Int): Flow<Boolean> = favoriteCitiesDao.observeIsFavorite(cityId)

    override suspend fun addToFavorite(city: City) {
        favoriteCitiesDao.addToFavorite(city.toDbModel())
    }

    override suspend fun removeFromFavorite(cityId: Int) {
        favoriteCitiesDao.removeFromFavorite(cityId)
    }
}