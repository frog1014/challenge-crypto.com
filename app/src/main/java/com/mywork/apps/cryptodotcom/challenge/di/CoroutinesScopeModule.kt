package com.mywork.apps.cryptodotcom.challenge.di

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob

@InstallIn(SingletonComponent::class)
@Module
class CoroutinesScopeModule {

    @ObserverCoroutineScope
    @Provides
    fun providesObserverCoroutineScope(@IoDispatcher ioDispatcher: CoroutineDispatcher): CoroutineScope =
        CoroutineScope(SupervisorJob() + ioDispatcher)
}

@InstallIn(ActivityComponent::class)
@Module
class MainCoroutinesScopeModule {

    @MainIoCoroutineScope
    @Provides
    fun providesMainIoCoroutineScope(@IoDispatcher ioDispatcher: CoroutineDispatcher): CoroutineScope =
        CoroutineScope(SupervisorJob() + ioDispatcher)
}

