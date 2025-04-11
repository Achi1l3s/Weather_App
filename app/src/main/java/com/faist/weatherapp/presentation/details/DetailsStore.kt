package com.faist.weatherapp.presentation.details

import android.util.Log
import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineBootstrapper
import com.arkivanov.mvikotlin.extensions.coroutines.CoroutineExecutor
import com.faist.weatherapp.domain.entity.City
import com.faist.weatherapp.domain.entity.Forecast
import com.faist.weatherapp.domain.usecase.ChangeFavoriteStateUseCase
import com.faist.weatherapp.domain.usecase.GetForecastUseCase
import com.faist.weatherapp.domain.usecase.ObserveFavoriteStateUseCase
import com.faist.weatherapp.presentation.details.DetailsStore.Intent
import com.faist.weatherapp.presentation.details.DetailsStore.Label
import com.faist.weatherapp.presentation.details.DetailsStore.State
import com.faist.weatherapp.presentation.details.DetailsStore.State.ForecastState.Initial
import kotlinx.coroutines.launch
import javax.inject.Inject

interface DetailsStore : Store<Intent, State, Label> {

    sealed interface Intent {

        data object ClickBack : Intent

        data object ClickChangeFavoriteStatus : Intent
    }

    data class State(
        val city: City,
        val isFavorite: Boolean,
        val forecastState: ForecastState
    ) {

        sealed interface ForecastState {

            data object Initial : ForecastState

            data object Loading : ForecastState

            data object Error : ForecastState

            data class Success(val forecast: Forecast) : ForecastState
        }
    }

    sealed interface Label {

        data object ClickBack : Label
    }
}

class DetailsStoreFactory @Inject constructor(
    private val storeFactory: StoreFactory,
    private val getForecastUseCase: GetForecastUseCase,
    private val changeFavoriteStateUseCase: ChangeFavoriteStateUseCase,
    private val observeFavoriteStateUseCase: ObserveFavoriteStateUseCase
) {

    fun create(city: City): DetailsStore =
        object : DetailsStore, Store<Intent, State, Label> by
        storeFactory.create(
            name = "DetailsStore",
            initialState = State(
                city = city,
                isFavorite = false,
                forecastState = Initial
            ),
            bootstrapper = BootstrapperImpl(city),
            executorFactory = ::ExecutorImpl,
            reducer = ReducerImpl
        ) {}

    private sealed interface Action {

        data class FavoriteStatusChanged(val isFavorite: Boolean) : Action

        data class ForecastLoaded(val forecast: Forecast) : Action

        data object ForecastStartLoading : Action

        data object ForecastLoadingError : Action
    }

    private sealed interface Msg {

        data class FavoriteStatusChanged(val isFavorite: Boolean) : Msg

        data class ForecastLoaded(val forecast: Forecast) : Msg

        data object ForecastStartLoading : Msg

        data object ForecastLoadingError : Msg
    }

    private inner class BootstrapperImpl(
        private val city: City
    ) : CoroutineBootstrapper<Action>() {
        override fun invoke() {
            scope.launch {
                observeFavoriteStateUseCase(city.id).collect {
                    dispatch(Action.FavoriteStatusChanged(it))
                }
            }
            scope.launch {
                dispatch(Action.ForecastStartLoading)
                try {
                    val forecast = getForecastUseCase(city.id)
//                    Log.d("MyDetailStore", "BootstrapperImpl forecast: ${forecast.upcoming.size}")
                    dispatch(Action.ForecastLoaded(forecast))
                } catch (e: Exception) {
                    dispatch(Action.ForecastLoadingError)
                }
            }
        }
    }

    private inner class ExecutorImpl : CoroutineExecutor<Intent, Action, State, Msg, Label>() {

        override fun executeIntent(intent: Intent, getState: () -> State) {
            when (intent) {
                Intent.ClickBack -> {
                    publish(Label.ClickBack)
                }

                Intent.ClickChangeFavoriteStatus -> {
                    val isFavorite = getState().isFavorite
                    val city = getState().city
                    scope.launch {
                        if (isFavorite) {
                            changeFavoriteStateUseCase.removeFromFavorite(city.id)
                        } else {
                            changeFavoriteStateUseCase.addToFavorite(city)
                        }
                    }
                }
            }
        }

        override fun executeAction(action: Action, getState: () -> State) {
            when (action) {
                is Action.FavoriteStatusChanged -> {
                    dispatch(Msg.FavoriteStatusChanged(action.isFavorite))
                }

                is Action.ForecastLoaded -> {
                    dispatch(Msg.ForecastLoaded(action.forecast))
                }

                Action.ForecastStartLoading -> {
                    dispatch(Msg.ForecastStartLoading)
                }

                Action.ForecastLoadingError -> {
                    dispatch(Msg.ForecastLoadingError)
                }
            }
        }
    }

    private object ReducerImpl : Reducer<State, Msg> {
        override fun State.reduce(msg: Msg): State = when (msg) {
            is Msg.FavoriteStatusChanged -> {
                copy(
                    isFavorite = msg.isFavorite
                )
            }

            is Msg.ForecastLoaded -> {
//                Log.d("MyDetailStore", "msg.forecast: ${msg.forecast}")
                copy(forecastState = State.ForecastState.Success(forecast = msg.forecast))
            }

            Msg.ForecastLoadingError -> {
                copy(forecastState = State.ForecastState.Error)
            }

            Msg.ForecastStartLoading -> {
                copy(forecastState = State.ForecastState.Loading)
            }
        }
    }
}