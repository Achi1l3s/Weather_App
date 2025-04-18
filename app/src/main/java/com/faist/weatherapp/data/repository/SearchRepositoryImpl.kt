package com.faist.weatherapp.data.repository

import com.faist.weatherapp.data.mapper.toEntities
import com.faist.weatherapp.data.network.api.ApiService
import com.faist.weatherapp.domain.entity.City
import com.faist.weatherapp.domain.repository.SearchRepository
import javax.inject.Inject

class SearchRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : SearchRepository {

    override suspend fun search(query: String): List<City> = apiService.searchCity(query).toEntities()
}