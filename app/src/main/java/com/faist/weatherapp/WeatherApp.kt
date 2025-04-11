package com.faist.weatherapp

import android.app.Application
import com.faist.weatherapp.di.ApplicationComponent
import com.faist.weatherapp.di.DaggerApplicationComponent

class WeatherApp : Application() {

    lateinit var applicationComponent: ApplicationComponent

    override fun onCreate() {
        super.onCreate()
        applicationComponent = DaggerApplicationComponent.factory().create(this)
    }
}