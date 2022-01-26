package com.example.ui.sensor

import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.domain.model.sensor.SensorConfigUiModel
import com.example.optumsoft.R
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

        /*mSocket = getSocket()
        Log.d("apple socket", mSocket.toString())
        mSocket?.connect()
        mSocket?.registerListener("data", onDataUpdatedListener)
        //mSocket?.registerListener("data", onDataUpdatedListener1)
        //mSocket?.registerListener("data", onDataUpdatedListener2)
        mSocket?.registerListener("connection", onSubscribeListener)
        mSocket?.subscribeToSensor("temperature0")*/
        //mSocket?.subscribeToSensor("temperature1")

        viewModel.getSensorList()
        viewModel.getSensorConfigList()
        viewModel.subscribeToSensorData()

        // register to listener

        lifecycleScope.launchWhenStarted {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.viewState.collect {
                    handleState(it)
                }
            }
        }
    }

    private fun handleState(uiState: MainViewState) {
        if (uiState.hasConfigData) {
            viewModel.subscribeSensors()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        //disconnectFromSocket(mSocket)
        //unregisterSensors(viewModel.getSensors())
        //viewModel.unsubscribeSensors()
        /*mSocket?.unregisterListener("data", onDataUpdatedListener)
        mSocket?.unregisterListener("connection", onSubscribeListener)
        mSocket?.unsubscribeFromSensor("temperature0")
        mSocket?.unsubscribeFromSensor("temperature1")*/
    }

    private val onSubscribeListener = Emitter.Listener { args ->
        //val jsonObj = args[0] as JSONObject
        Log.d("orange", "onConnectionListener")
        //Log.d("apple", "onConnectionListener")
    }

    private val onDataUpdatedListener = Emitter.Listener { args ->
        val gson = GsonBuilder().create()
        Log.d("apple", "onDataUpdateListener")
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

    private val onDataUpdatedListener1 = Emitter.Listener { args ->
        val gson = GsonBuilder().create()
        Log.d("apple", "onDataUpdateListener")
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

    private val onDataUpdatedListener2 = Emitter.Listener { args ->
        val gson = GsonBuilder().create()
        Log.d("apple", "onDataUpdateListener")
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

    private fun unregisterSensors(sensorList: List<SensorConfigUiModel>) {
        Log.d("unregister123 size", sensorList.size.toString())
        sensorList.forEach {
            Log.d("unregister123", it.name)
            mSocket?.unsubscribeFromSensor(it.name)
        }
    }
}