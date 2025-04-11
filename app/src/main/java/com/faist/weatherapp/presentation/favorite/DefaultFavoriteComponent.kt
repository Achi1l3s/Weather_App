package com.faist.weatherapp.presentation.favorite

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.instancekeeper.getStore
import com.arkivanov.mvikotlin.extensions.coroutines.labels
import com.arkivanov.mvikotlin.extensions.coroutines.stateFlow
import com.faist.weatherapp.domain.entity.City
import com.faist.weatherapp.presentation.extensions.componentScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DefaultFavoriteComponent @AssistedInject constructor(
    private val favoriteStoreFactory: FavoriteStoreFactory,
    @Assisted("onCityItemClicked") private val onCityItemClicked: (City) -> Unit,
    @Assisted("onAddFavoriteClicked") private val onAddFavoriteClicked: () -> Unit,
    @Assisted("onSearchClicked") private val onSearchClicked: () -> Unit,
    @Assisted("componentContext") componentContext: ComponentContext
) : FavoriteComponent, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore { favoriteStoreFactory.create() }
    private val scope = componentScope()

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    is FavoriteStore.Label.CityItemClicked -> {
                        onCityItemClicked(it.city)
                    }

                    FavoriteStore.Label.ClickAddToFavorite -> {
                        onAddFavoriteClicked()
                    }

                    FavoriteStore.Label.ClickSearch -> {
                        onSearchClicked()
                    }
                }
            }
        }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<FavoriteStore.State>
        get() = store.stateFlow

    override fun onClickSearch() {
        store.accept(FavoriteStore.Intent.ClickSearch)
    }

    override fun onClickAddFavorite() {
        store.accept(FavoriteStore.Intent.ClickAddToFavorite)
    }

    override fun onCityItemClick(city: City) {
        store.accept(FavoriteStore.Intent.CityItemClicked(city))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onCityItemClicked") onCityItemClicked: (City) -> Unit,
            @Assisted("onAddFavoriteClicked") onAddFavoriteClicked: () -> Unit,
            @Assisted("onSearchClicked") onSearchClicked: () -> Unit,
            @Assisted("componentContext") componentContext: ComponentContext
        ) : DefaultFavoriteComponent
    }
}