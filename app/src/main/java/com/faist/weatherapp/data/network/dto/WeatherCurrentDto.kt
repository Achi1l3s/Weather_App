package com.faist.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class WeatherCurrentDto(
    @SerializedName("current") val currentDay: WeatherDto
)
