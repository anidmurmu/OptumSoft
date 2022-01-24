package com.example.data.repositoryimpl.socket.di

import com.example.data.repositoryimpl.socket.SocketRepositoryImpl
import com.example.data.source.network.socket_io.SocketSubscriberSrc
import com.example.domain.repository.socket.SocketRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityRetainedComponent
import dagger.hilt.android.scopes.ActivityRetainedScoped

@Module
@InstallIn(ActivityRetainedComponent::class)
class SocketRepositoryModule {

    @Provides
    @ActivityRetainedScoped
    fun provideSocketRepository(
        socketSubscriberSrc: SocketSubscriberSrc
    ): SocketRepository {
        return SocketRepositoryImpl(socketSubscriberSrc)
    }
}