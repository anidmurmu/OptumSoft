package com.example.data.source.network.socket_io

import android.util.Log
import com.example.data.entity.sensor.SensorNetworkModel
import com.example.domain.model.response.Response
import com.google.gson.Gson
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SubscribeToSensorDataSrc @Inject constructor(
    private val socket: Socket?,
    private val gson: Gson
) {
    fun subscribeToSensorDataSrc() = callbackFlow<Response<SensorNetworkModel>> {
        Log.d("pear3", "pear3")
        //val socket = getSocket()
        Log.d("pear5 socket", "pear $socket")
        socket.subscribeToSensor("temperature0")

        val listener = Emitter.Listener { args ->
            Log.d("pear4", "pear4")
            val result = try {
                val jsonStr = args[0].toString()
                val pojo = gson.fromJson(jsonStr, SensorNetworkModel::class.java)
                Log.d("pear1", pojo.toString())
                Response.Success(pojo)
            } catch (ex: Exception) {
                Response.Failure(ex)
            }
            trySend(result)
        }

        socket?.registerListener("data", listener)
        awaitClose { socket?.unregisterListener("data", listener)}


    }
}