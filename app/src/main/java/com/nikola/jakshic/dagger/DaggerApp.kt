package com.nikola.jakshic.dagger

import android.app.Application
import coil.Coil
import coil.ImageLoader
import com.nikola.jakshic.dagger.di.AppComponent
import com.nikola.jakshic.dagger.di.DaggerAppComponent
import com.nikola.jakshic.dagger.di.NetworkModule
import javax.inject.Inject

class DaggerApp : Application() {

    lateinit var appComponent: AppComponent
        private set

    @Inject lateinit var imageLoader: ImageLoader

    override fun onCreate() {
        super.onCreate()

        appComponent = DaggerAppComponent.builder()
            .networkModule(NetworkModule(this))
            .build()
        appComponent.inject(this)

        Coil.setImageLoader(imageLoader)
    }
}