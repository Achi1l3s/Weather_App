package com.faist.weatherapp.data.network.dto

import com.google.gson.annotations.SerializedName

data class DayWeatherDto(
    @SerializedName("avgtemp_c") val averageTempC: Float,
    @SerializedName("condition") val condition: ConditionDto
)