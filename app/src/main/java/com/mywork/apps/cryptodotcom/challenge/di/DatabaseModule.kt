package com.mywork.apps.cryptodotcom.challenge.di

import android.content.Context
import com.mywork.apps.cryptodotcom.challenge.data.AppDatabase
import com.mywork.apps.cryptodotcom.challenge.data.dao.CurrencyDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@InstallIn(SingletonComponent::class)
@Module
class DatabaseModule {

    @Singleton
    @Provides
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        AppDatabase.getInstance(context)

    @Provides
    fun provideCurrencyDao(appDatabase: AppDatabase): CurrencyDao = appDatabase.currencyDao()
}
