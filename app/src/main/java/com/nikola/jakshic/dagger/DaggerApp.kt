package com.nikola.jakshic.dagger

import android.app.Application
import com.nikola.jakshic.dagger.di.AppComponent
import com.nikola.jakshic.dagger.di.DaggerAppComponent
import com.nikola.jakshic.dagger.di.NetworkModule

class DaggerApp : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
                .networkModule(NetworkModule(this))
                .build()
    }
}