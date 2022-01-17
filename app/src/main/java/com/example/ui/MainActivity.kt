package com.example.ui

import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.optumsoft.R
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
        connectToSocket(mSocket)
        mSocket?.subscribeToSensor("temperature0")
        mSocket?.registerListener("connection", onSubscribeListener)

        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect { uiState ->
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
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        disconnectFromSocket(mSocket)
        mSocket?.unsubscribeFromSensor("temperature0")
        mSocket?.unregisterListener("connection", onSubscribeListener)
    }

    private val onSubscribeListener = Emitter.Listener { args ->
        //val jsonObj = args[0] as JSONObject
        Log.d("apple", "onConnectionListener")
        runOnUiThread(Runnable {
            Log.d("apple", "onConnectionListener")
            Toast.makeText(this, "toast", Toast.LENGTH_SHORT).show()
        })
    }

    private val onDataUpdatedListener = Emitter.Listener { args ->

    }
}