package com.faist.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class WeatherForecastDto(
//    @SerializedName("location") val location: CityDto,
    @SerializedName("current") val current: WeatherDto,
    @SerializedName("forecast") val forecast: ForecastDto
)
