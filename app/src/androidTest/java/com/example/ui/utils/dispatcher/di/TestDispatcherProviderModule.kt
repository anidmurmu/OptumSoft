package com.example.ui.utils.dispatcher.di

import com.example.ui.utils.dispatcher.DispatcherProvider
import com.example.ui.utils.dispatcher.TestDispatcherProvider
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.ExperimentalCoroutinesApi
import javax.inject.Named
import javax.inject.Singleton

const val TEST_DISPATCHER = "test_dispatcher"

@Module
@InstallIn(SingletonComponent::class)
@ExperimentalCoroutinesApi
object TestDispatcherProviderModule {

    @Provides
    @Singleton
    @Named(TEST_DISPATCHER)
    fun provideTestDispatcherProvider(): DispatcherProvider {
        return TestDispatcherProvider()
    }
}