package com.example.domain.usecase.socket

import com.example.domain.repository.socket.SocketRepository
import javax.inject.Inject

interface SubscribeToSensorUseCase {
    suspend fun subscribeToSensor(
        sensorName: String,
        shouldSubscribe: Boolean = true
    ): Boolean
}

class SubscribeToSensorUseCaseImpl @Inject constructor(
    private val socketRepository: SocketRepository
) : SubscribeToSensorUseCase {
    override suspend fun subscribeToSensor(sensorName: String, shouldSubscribe: Boolean)
            : Boolean {
        return socketRepository.subscribeToSensor(sensorName, shouldSubscribe)
    }
}