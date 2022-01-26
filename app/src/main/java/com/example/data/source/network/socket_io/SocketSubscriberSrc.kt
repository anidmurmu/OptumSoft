package com.example.data.source.network.socket_io

import io.socket.client.Socket
import javax.inject.Inject

class SocketSubscriberSrc @Inject constructor(
    private val socket: Socket?
) {
    fun subscribeToSensor(sensorName: String, shouldScribe: Boolean): Boolean {
        var isConnected = false
        socket?.apply {
            connect()
            if (shouldScribe) {
                subscribeToSensor(sensorName)
            } else {
                unsubscribeFromSensor(sensorName)
            }
            isConnected = true
        }
        return isConnected
    }
}