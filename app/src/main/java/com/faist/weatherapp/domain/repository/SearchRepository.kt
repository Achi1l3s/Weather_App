package com.faist.weatherapp.domain.repository

import com.faist.weatherapp.domain.entity.City

interface SearchRepository {

    suspend fun search(query: String): List<City>
}