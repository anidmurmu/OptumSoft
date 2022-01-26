package com.example.data.source.network.socket_io

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import io.socket.client.IO
import io.socket.client.Socket
import java.net.URISyntaxException
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class SocketModule {

    @Provides
    @Singleton
    fun provideSocketIO(): Socket? {
        var socket: Socket? = null
        try {
            socket = IO.socket("http://interview.optumsoft.com")
        } catch (e: URISyntaxException) {
        }
        return socket
    }
}