package com.faist.weatherapp.data.mapper

import com.faist.weatherapp.data.network.dto.WeatherCurrentDto
import com.faist.weatherapp.data.network.dto.WeatherDto
import com.faist.weatherapp.data.network.dto.WeatherForecastDto
import com.faist.weatherapp.domain.entity.Forecast
import com.faist.weatherapp.domain.entity.Weather
import java.util.Calendar
import java.util.Date

fun WeatherCurrentDto.toEntity(): Weather = currentDay.toEntity()

fun WeatherDto.toEntity(): Weather = Weather(
    tempC = tempC,
    conditionUrl = conditionDto.iconUrl.correctImgUrl(),
    conditionText = conditionDto.text,
    date = date.toCalendar(),
    wind = windKph,
    humidity = humidity
)

fun WeatherForecastDto.toEntity(): Forecast {
//    Log.d("MyDetailStore", "MAPPER forecast.forecastDay.size: ${forecast.forecastDay.size}")
    return Forecast(
        current.toEntity(),
        forecastDto.forecastDay.map { dayDto ->
            val dayWeatherDto = dayDto.dayWeatherDto
            Weather(
                tempC = dayWeatherDto.averageTempC,
                conditionText = dayWeatherDto.condition.text,
                conditionUrl = dayWeatherDto.condition.iconUrl.correctImgUrl(),
                date = dayDto.date.toCalendar(),
                wind = dayWeatherDto.windKph,
                humidity = dayWeatherDto.humidity
            )
        }
    )
}


private fun Long.toCalendar() = Calendar.getInstance().apply {
    time = Date(this@toCalendar * 1000)
}

private fun String.correctImgUrl(): String {
    return "https:$this"
        .replace(
            "64x64",
            "128x128"
        )
}