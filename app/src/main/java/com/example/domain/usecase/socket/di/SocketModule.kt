package com.example.domain.usecase.socket.di

import com.example.domain.repository.socket.SocketRepository
import com.example.domain.usecase.socket.SubscribeToSensorUseCase
import com.example.domain.usecase.socket.SubscribeToSensorUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class SocketModule {

    @Provides
    @ActivityRetainedScoped
    fun provideSubscribeToSensorUseCase(
        socketRepository: SocketRepository
    ): SubscribeToSensorUseCase {
        return SubscribeToSensorUseCaseImpl(
            socketRepository
        )
    }
}