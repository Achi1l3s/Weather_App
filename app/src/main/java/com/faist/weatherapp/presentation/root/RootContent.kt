package com.faist.weatherapp.presentation.root

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.faist.weatherapp.presentation.details.DetailsContent
import com.faist.weatherapp.presentation.favorite.FavoriteContent
import com.faist.weatherapp.presentation.search.SearchContent
import com.faist.weatherapp.presentation.ui.theme.WeatherAppTheme
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun RootContent(component: RootComponent) {

    WeatherAppTheme {
        val systemUiController = rememberSystemUiController()
        systemUiController.setStatusBarColor(
            color = MaterialTheme.colorScheme.background,
            darkIcons = true
        )

        Children(
            stack = component.stack
        ) {
            when (val instance = it.instance) {
                is RootComponent.Child.Details -> {
                    DetailsContent(component = instance.component)
                }

                is RootComponent.Child.Favorite -> {
                    FavoriteContent(component = instance.component)
                }

                is RootComponent.Child.Search -> {
                    SearchContent(component = instance.component)
                }
            }
        }
    }
}