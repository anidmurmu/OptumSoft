package com.example.domain.repository.socket

interface SocketRepository {
    suspend fun subscribeToSensor(sensorName: String, shouldSubscribe: Boolean): Boolean
}