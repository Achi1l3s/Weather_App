package com.faist.weatherapp.presentation.details

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.faist.weatherapp.R
import com.faist.weatherapp.domain.entity.Forecast
import com.faist.weatherapp.domain.entity.Weather
import com.faist.weatherapp.presentation.extensions.changeTallinnNameToCorrect
import com.faist.weatherapp.presentation.extensions.formattedFullDate
import com.faist.weatherapp.presentation.extensions.formattedShortDayOfWeek
import com.faist.weatherapp.presentation.extensions.humidityToFormattedString
import com.faist.weatherapp.presentation.extensions.tempToFormattedString
import com.faist.weatherapp.presentation.extensions.windToFormattedString
import com.faist.weatherapp.presentation.ui.theme.CardGradients

@Composable
fun DetailsContent(component: DetailsComponent) {
    val state by component.model.collectAsState()

    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .background(brush = CardGradients.gradients[1].primaryGradient),
        contentColor = MaterialTheme.colorScheme.background,
        containerColor = Color.Transparent,
        topBar = {
            TopBar(
                cityName = state.city.name.changeTallinnNameToCorrect(),
                isCityFavorite = state.isFavorite,
                onBackClick = { component.onClickBack() },
                onClickChangeFavoriteState = { component.onClickChangeFavoriteState() }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .padding(paddingValues),
        ) {
            when (val forecastState = state.forecastState) {
                DetailsStore.State.ForecastState.Error -> {
                    Error()
                }

                DetailsStore.State.ForecastState.Initial -> {
                    Initial()
                }

                DetailsStore.State.ForecastState.Loading -> {
                    Loading()
                }

                is DetailsStore.State.ForecastState.Success -> {
                    Forecast(forecast = forecastState.forecast)
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun TopBar(
    cityName: String,
    isCityFavorite: Boolean,
    onBackClick: () -> Unit,
    onClickChangeFavoriteState: () -> Unit
) {
    CenterAlignedTopAppBar(
        title = { Text(text = cityName) },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = MaterialTheme.colorScheme.background
        ),
        navigationIcon = {
            IconButton(onClick = { onBackClick() }) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background
                )
            }
        },
        actions = {
            IconButton(onClick = { onClickChangeFavoriteState() }) {
                val icon =
                    if (isCityFavorite) {
                        Icons.Default.Star
                    } else {
                        Icons.Default.StarBorder
                    }
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.background
                )
            }
        }
    )
}

@Composable
private fun Loading() {
    Box(
        modifier = Modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(color = MaterialTheme.colorScheme.background)
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun Forecast(forecast: Forecast) {
    Column(
        modifier = Modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally

    ) {
        Spacer(modifier = Modifier.weight(1f))
        Text(
            text = forecast.currentWeather.conditionText,
            style = MaterialTheme.typography.titleLarge
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = forecast.currentWeather.tempC.tempToFormattedString(),
                style = MaterialTheme.typography.headlineLarge.copy(fontSize = 70.sp)
            )
            GlideImage(
                modifier = Modifier.size(70.dp),
                model = forecast.currentWeather.conditionUrl,
                contentDescription = null
            )
        }
        Text(
            modifier = Modifier,
            text = forecast.currentWeather.wind.windToFormattedString()
        )
        Text(
            modifier = Modifier
                .padding(bottom = 16.dp),
            text = forecast.currentWeather.humidity.humidityToFormattedString()
        )
        Text(
            text = forecast.currentWeather.date.formattedFullDate(),
            style = MaterialTheme.typography.titleLarge
        )
        Spacer(modifier = Modifier.weight(1f))
        AnimateUpcomingWeather(forecast.upcoming)
        Spacer(modifier = Modifier.weight(0.5f))
    }
}

@Composable
private fun AnimateUpcomingWeather(upcoming: List<Weather>) {
    val state = remember {
        MutableTransitionState(false).apply {
            targetState = true
        }
    }

    AnimatedVisibility(
        visibleState = state,
        enter = fadeIn(
            animationSpec = tween(500)
        ) + slideIn(
            animationSpec = tween(500),
            initialOffset = {
                IntOffset(0, it.height)
            }
        )
    ) {
        UpcomingWeather(upcoming)
    }
}

@Composable
private fun UpcomingWeather(upcoming: List<Weather>) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(24.dp),
        shape = MaterialTheme.shapes.extraLarge,
        colors = CardDefaults.cardColors()
            .copy(
                containerColor = MaterialTheme.colorScheme.background.copy(alpha = 0.24f)
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp)
        ) {
            Text(
                modifier = Modifier
                    .padding(bottom = 24.dp)
                    .align(Alignment.CenterHorizontally),
                text = stringResource(R.string.upcoming),
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.background
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                upcoming.forEach {
                    SmallWeatherCard(it)
                }
            }
        }
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
private fun RowScope.SmallWeatherCard(weather: Weather) {
    Card(
        modifier = Modifier
            .height(128.dp)
            .weight(1f),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Text(text = weather.tempC.tempToFormattedString())
            GlideImage(
                modifier = Modifier.size(48.dp),
                model = weather.conditionUrl,
                contentDescription = null
            )
            Text(text = weather.date.formattedShortDayOfWeek())
        }
    }
}

@Composable
private fun Initial() {
}

@Composable
private fun Error() {
}