package com.example.ui

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.optumsoft.R
import com.google.gson.GsonBuilder
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket
import io.socket.emitter.Emitter
import kotlinx.coroutines.launch
import org.json.JSONObject


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var mSocket: Socket? = null

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val dummyTextView = findViewById<TextView>(R.id.tvDummyText)

        mSocket = getSocket()
        mSocket?.subscribeToSensor("temperature0")
        mSocket?.subscribeToSensor("temperature1")
        mSocket?.registerListener("connection", onSubscribeListener)
        mSocket?.registerListener("data", onDataUpdatedListener)
        connectToSocket(mSocket)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {

            }
        }

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
        Log.d("apple", "onConnectionListener")
    }

    private val onDataUpdatedListener = Emitter.Listener { args ->
        //Log.d("apple", "onDataUpdateListener")
        val jsonObject = args[0] as JSONObject
        val gson = GsonBuilder().create()
        val pojo = gson.toJson(jsonObject)
        Log.d("apple pojo", pojo.toString())
    }
}