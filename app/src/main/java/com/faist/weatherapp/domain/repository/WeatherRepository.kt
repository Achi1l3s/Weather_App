package com.faist.weatherapp.domain.repository

import com.faist.weatherapp.domain.entity.Forecast
import com.faist.weatherapp.domain.entity.Weather

interface WeatherRepository {

    suspend fun getWeather(cityId: Int): Weather

    suspend fun getForecast(cityId: Int): Forecast
}