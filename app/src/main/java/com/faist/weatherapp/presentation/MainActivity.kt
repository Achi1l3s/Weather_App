package com.faist.weatherapp.presentation

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.faist.weatherapp.data.network.api.ApiFactory
import com.faist.weatherapp.presentation.ui.theme.WeatherAppTheme
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val apiService = ApiFactory.apiService
        CoroutineScope(Dispatchers.Main).launch {
            val currentWeather = apiService.loadCurrentLocation("Tallinn")
            val forecast = apiService.loadForecast("Tallinn")
            val cities = apiService.searchCity("New York")
            Log.d("CHECK_API",
                "Current Weather: $currentWeather,\n" +
                        "Forecast: $forecast,\n" +
                        "Cities: $cities")
        }

        setContent {
            WeatherAppTheme {

            }
        }
    }
}
