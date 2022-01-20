package com.example.ui.sensor

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.optumsoft.R
import com.example.ui.*
import com.example.ui.utils.socket.*
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONObject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var mSocket: Socket? = null

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.getSensorList()

        val dummyTextView = findViewById<TextView>(R.id.tvDummyText)

        mSocket = getSocket()
        mSocket?.subscribeToSensor("temperature0")
        mSocket?.subscribeToSensor("temperature1")
        mSocket?.registerListener("connection", onSubscribeListener)
        mSocket?.registerListener("data", onDataUpdatedListener)
        connectToSocket(mSocket)

        val socket = getSocket1("/sensornames")
        socket?.on(Socket.EVENT_CONNECT, Emitter.Listener {
            val result = it as Array<*>
            //Log.d("endpoint", result.toString())
            Log.d("endpoint", "apple")
        })

        socket?.registerListener("connection") {
            Log.d("pineapple", "pineapple")
        }
        socket?.connect()

        viewModel.viewState.observe(this) { uiState ->
            when (uiState) {
                is MainViewState.Initial -> {
                }
                is MainViewState.Success -> {
                    dummyTextView.text = uiState.str
                }
                is MainViewState.Failure -> {

                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disconnectFromSocket(mSocket)
        mSocket?.unsubscribeFromSensor("temperature0")
        mSocket?.unsubscribeFromSensor("temperature1")
        mSocket?.unregisterListener("connection", onSubscribeListener)
        mSocket?.unregisterListener("data", onDataUpdatedListener)
    }

    private val onSubscribeListener = Emitter.Listener { args ->
        //val jsonObj = args[0] as JSONObject
        Log.d("orange", "onConnectionListener")
    }

    private val onDataUpdatedListener = Emitter.Listener { args ->
        val gson = GsonBuilder().create()
        //Log.d("apple", "onDataUpdateListener")
        Log.d("dekho", args[0].toString())
        //val jsonStr = gson.to
        val jsonStr = args[0].toString()
        val jsonObject = args[0] as JSONObject
        val json = gson.toJson(jsonObject)
        val jsonPojo = gson.fromJson(jsonStr, NetworkModel::class.java)
        //val pojo = gson.fromJson(jsonStr, NetworkModel::class.java)
        //val pojo = gson.toJsonTree(NetworkModel::class.java)
        Log.d("apple json", json)
        Log.d("apple jsonobj", jsonObject.toString())
        Log.d("apple jsonPojo", jsonPojo.toString())
        //Log.d("apple pojo", pojo.toString())
        //if (pojo.)
    }
}