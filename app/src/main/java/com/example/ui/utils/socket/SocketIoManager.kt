package com.example.ui.utils.socket

import android.util.Log
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import java.net.URI
import java.net.URISyntaxException

fun getSocket(): Socket? {
    var socket: Socket? = null
    try {
        socket = IO.socket("http://interview.optumsoft.com")
        Log.d("apple", "connected")
    } catch (e: URISyntaxException) {
        Log.d("apple", "connection failed")
    }
    return socket
}

fun connectToSocket(socket: Socket?) {
    socket?.connect()
}

fun disconnectFromSocket(socket: Socket?) {
    socket?.disconnect()
}

fun Socket?.subscribeToSensor(sensorName: String) {
    this?.emit("subscribe", sensorName)
}

fun Socket?.unsubscribeFromSensor(sensorName: String) {
    this?.emit("unsubscribe", sensorName)
}

fun Socket?.registerListener(eventName: String, eventListener: Emitter.Listener) {
    this?.on(eventName, eventListener)
}
fun Socket?.unregisterListener(eventName: String, eventListener: Emitter.Listener) {
    this?.off(eventName, eventListener)
}

fun getSocket1(endPoint: String): Socket? {
    var socket: Socket? = null
    try {
        val options = IO.Options.builder()
            .setPath(endPoint)
            .build()
        //socket = IO.socket("http://interview.optumsoft.com$endPoint")
        socket = IO.socket(URI.create("http://interview.optumsoft.com"), options)
        Log.d("endpoint1", "connected")
    } catch (e: URISyntaxException) {
        Log.d("endpoint1", "connection failed")
    }
    return socket
}