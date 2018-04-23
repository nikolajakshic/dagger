package com.nikola.jakshic.dagger

import android.app.Application
import com.crashlytics.android.Crashlytics
import com.nikola.jakshic.dagger.di.AppComponent
import com.nikola.jakshic.dagger.di.DaggerAppComponent
import com.nikola.jakshic.dagger.di.NetworkModule
import io.fabric.sdk.android.Fabric

class DaggerApp : Application() {

    lateinit var appComponent: AppComponent
        private set

    override fun onCreate() {
        super.onCreate()

        if (!BuildConfig.DEBUG) Fabric.with(this, Crashlytics())

        appComponent = DaggerAppComponent.builder()
                .networkModule(NetworkModule(this))
                .build()
    }
}