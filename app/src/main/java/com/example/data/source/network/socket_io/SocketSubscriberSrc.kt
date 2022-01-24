package com.example.data.source.network.socket_io

import javax.inject.Inject

class SocketSubscriberSrc @Inject constructor() {
    fun subscribeToSensor(sensorName: String, shouldScribe: Boolean): Boolean {
        var isConnected = false
        val socket = getSocket()
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