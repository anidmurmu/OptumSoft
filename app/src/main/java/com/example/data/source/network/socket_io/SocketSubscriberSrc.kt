package com.example.data.source.network.socket_io

import android.util.Log
import io.socket.client.Socket
import javax.inject.Inject

class SocketSubscriberSrc @Inject constructor(
    private val socket: Socket?
) {
    fun subscribeToSensor(sensorName: String, shouldScribe: Boolean): Boolean {
        var isConnected = false
        //val socket = getSocket()
        socket?.apply {
            //Log.d("pear6", "connect or not")
            connect()
            if (shouldScribe) {
                Log.d("pear7", "$sensorName")
                subscribeToSensor(sensorName)
            } else {
                unsubscribeFromSensor(sensorName)
            }
            isConnected = true
        }
        return isConnected
    }
}