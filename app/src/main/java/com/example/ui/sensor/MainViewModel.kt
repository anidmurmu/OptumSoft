package com.example.ui.sensor

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.response.Response
import com.example.domain.model.sensor.SensorConfigUiModel
import com.example.domain.usecase.sensor.GetSensorConfigListUseCase
import com.example.domain.usecase.sensor.GetSensorListUseCase
import com.example.domain.usecase.socket.SubscribeToSensorUseCase
import com.example.ui.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val dispatcherProvider: DispatcherProvider,
    private val getSensorListUseCase: GetSensorListUseCase,
    private val getSensorConfigListUseCase: GetSensorConfigListUseCase,
    private val subscribeToSensorUseCase: SubscribeToSensorUseCase
) : ViewModel() {

    private val _viewState: MutableStateFlow<MainViewState> =
        MutableStateFlow(MainViewState())
    val viewState: StateFlow<MainViewState> = _viewState


    fun getSensorList() {
        viewModelScope.launch(dispatcherProvider.io) {
            getSensorListUseCase.getSensorList()
                .onFailure {
                    Log.e("apple1", (it as Exception).toString())
                }
                .onSuccess {
                    Log.d("apple2", it.toString())
                }
        }
    }

    fun getSensorConfigList() {
        viewModelScope.launch(dispatcherProvider.io) {
            val result = getSensorConfigListUseCase.getSensorConfigList()
            when (result) {
                is Response.Failure -> {
                    Log.e("orange", result.error.message.toString())
                }
                is Response.Success -> {
                    _viewState.value = _viewState.value.copy(configList = result.data)
                    _viewState.value = _viewState.value.copy(hasConfigData = true)
                }

            }
        }
    }

    fun getSensors(): List<SensorConfigUiModel> {
        return _viewState.value.configList
    }

    fun subscribeSensors() {
        val sensorList = getSensors()
        viewModelScope.launch(dispatcherProvider.io) {
            sensorList.forEach {
                subscribeToSensorUseCase.subscribeToSensor(it.name)
            }
        }
    }
}