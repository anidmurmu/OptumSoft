package com.example.ui.sensor

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.optumsoft.R
import dagger.hilt.android.AndroidEntryPoint
import io.socket.client.Socket


@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private var mSocket: Socket? = null

    private val viewModel: MainViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        viewModel.getSensorList()
        viewModel.getSensorConfigList()
        viewModel.subscribeToSensorData()

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
}