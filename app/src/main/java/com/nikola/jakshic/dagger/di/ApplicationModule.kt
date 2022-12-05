package com.nikola.jakshic.dagger.di

import com.nikola.jakshic.dagger.common.Dispatchers
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {
    @Provides
    @Singleton
    fun provideApplicationScope(dispatchers: Dispatchers): CoroutineScope {
        return CoroutineScope(dispatchers.main + SupervisorJob())
    }
}
