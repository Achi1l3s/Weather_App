package com.faist.weatherapp.di

import android.content.Context
import com.faist.weatherapp.data.local.db.FavoriteCitiesDao
import com.faist.weatherapp.data.local.db.FavoriteDatabase
import com.faist.weatherapp.data.network.api.ApiFactory
import com.faist.weatherapp.data.network.api.ApiService
import com.faist.weatherapp.data.repository.FavoriteRepositoryImpl
import com.faist.weatherapp.data.repository.SearchRepositoryImpl
import com.faist.weatherapp.data.repository.WeatherRepositoryImpl
import com.faist.weatherapp.domain.repository.FavoriteRepository
import com.faist.weatherapp.domain.repository.SearchRepository
import com.faist.weatherapp.domain.repository.WeatherRepository
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module
interface DataModule {

    @[ApplicationScope Binds]
    fun bindFavoriteRepository(impl: FavoriteRepositoryImpl): FavoriteRepository

    @[ApplicationScope Binds]
    fun bindWeatherRepository(impl: WeatherRepositoryImpl): WeatherRepository

    @[ApplicationScope Binds]
    fun bindSearchRepository(impl: SearchRepositoryImpl): SearchRepository

    companion object {

        @[ApplicationScope Provides]
        fun provideApiService(): ApiService = ApiFactory.apiService

        @[ApplicationScope Provides]
        fun provideFavoriteDatabase(context: Context): FavoriteDatabase {
            return FavoriteDatabase.getInstance(context)
        }

        @[ApplicationScope Provides]
        fun provideFavoriteCitiesDao(database: FavoriteDatabase): FavoriteCitiesDao {
            return database.favoriteCitiesDao()
        }
    }
}