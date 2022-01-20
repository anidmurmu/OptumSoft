package com.example.ui.utils.dispatcher.di

import com.example.ui.utils.dispatcher.DispatcherProvider
import com.example.ui.utils.dispatcher.StandardDispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DispatcherProviderModule {

    @Provides
    @Singleton
    fun provideStandardDispatcherProvider(): DispatcherProvider {
        return StandardDispatcherProvider()
    }
}