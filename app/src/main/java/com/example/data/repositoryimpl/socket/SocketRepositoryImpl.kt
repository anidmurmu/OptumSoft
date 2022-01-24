package com.example.data.repositoryimpl.socket

import com.example.data.source.network.socket_io.SocketSubscriberSrc
import com.example.domain.repository.socket.SocketRepository
import javax.inject.Inject

class SocketRepositoryImpl @Inject constructor(
    private val socketSubscriberSrc: SocketSubscriberSrc
) : SocketRepository {
    override suspend fun subscribeToSensor(
        sensorName: String,
        shouldSubscribe: Boolean
    ): Boolean {
        return socketSubscriberSrc.subscribeToSensor(sensorName, shouldSubscribe)
    }
}