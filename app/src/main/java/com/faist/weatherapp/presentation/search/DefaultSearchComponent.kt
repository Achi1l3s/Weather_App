package com.faist.weatherapp.presentation.search

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

class DefaultSearchComponent @AssistedInject constructor(
    private val searchStoreFactory: SearchStoreFactory,
    @Assisted("onBackClicked") private val onBackClicked: () -> Unit,
    @Assisted("onCitySavedToFavorite") private val onCitySavedToFavorite: () -> Unit,
    @Assisted("onForecastForCityRequested") private val onForecastForCityRequested: (City) -> Unit,
    @Assisted("openReason") private val openReason: OpenReason,
    @Assisted("componentContext") componentContext: ComponentContext
) : SearchComponent, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getStore { searchStoreFactory.create(openReason) }
    private val scope = componentScope()

    @OptIn(ExperimentalCoroutinesApi::class)
    override val model: StateFlow<SearchStore.State>
        get() = store.stateFlow

    init {
        scope.launch {
            store.labels.collect {
                when (it) {
                    SearchStore.Label.ClickBack -> {
                        onBackClicked()
                    }

                    is SearchStore.Label.OpenForecast -> {
                        onForecastForCityRequested(it.city)
                    }

                    SearchStore.Label.SavedToFavorite -> {
                        onCitySavedToFavorite()
                    }
                }
            }
        }
    }

    override fun changeSearchQuery(query: String) {
        store.accept(SearchStore.Intent.ChangeSearchQuery(query))
    }

    override fun onClickBack() {
        store.accept(SearchStore.Intent.ClickBack)
    }

    override fun onClickSearch() {
        store.accept(SearchStore.Intent.ClickSearch)
    }

    override fun onClickCity(city: City) {
        store.accept(SearchStore.Intent.ClickCity(city))
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("onBackClicked") onBackClicked: () -> Unit,
            @Assisted("onCitySavedToFavorite") onCitySavedToFavorite: () -> Unit,
            @Assisted("onForecastForCityRequested") onForecastForCityRequested: (City) -> Unit,
            @Assisted("openReason") openReason: OpenReason,
            @Assisted("componentContext") componentContext: ComponentContext
        ) : DefaultSearchComponent
    }
}