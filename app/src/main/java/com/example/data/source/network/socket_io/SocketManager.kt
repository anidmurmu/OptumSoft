package com.example.data.source.network.socket_io

import io.socket.client.Socket
import io.socket.emitter.Emitter

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