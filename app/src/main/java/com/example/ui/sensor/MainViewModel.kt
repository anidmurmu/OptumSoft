package com.example.ui.sensor

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.domain.model.response.Response
import com.example.domain.model.sensor.SensorConfigUiModel
import com.example.domain.model.sensor.SensorUiModel
import com.example.domain.usecase.sensor.GetSensorConfigListUseCase
import com.example.domain.usecase.sensor.GetSensorListUseCase
import com.example.domain.usecase.sensor.SubscribeForSensorDataUseCase
import com.example.domain.usecase.socket.SubscribeToSensorUseCase
import com.example.ui.utils.base.recyclerview.BaseBindingRVModel
import com.example.ui.utils.base.viewmodel.BaseViewModel
import com.example.ui.utils.dispatcher.DispatcherProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    application: Application,
    private val dispatcherProvider: DispatcherProvider,
    private val getSensorListUseCase: GetSensorListUseCase,
    private val getSensorConfigListUseCase: GetSensorConfigListUseCase,
    private val subscribeToSensorUseCase: SubscribeToSensorUseCase,
    private val subscribeForSensorDataUseCase: SubscribeForSensorDataUseCase
) : BaseViewModel(application) {

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
                    val modelList = namesToViewableList(it)
                    _viewState.value.sensorNameList.postValue(modelList)
                }
        }
    }

    private fun namesToViewableList(nameList: List<String>): List<BaseBindingRVModel> {
        return nameList.map {
            val sensorModel = SensorUiModel(
                it,
                null,
                null,
                false,
                "",
                null,
                null
            )
            SensorRVModel(sensorModel)
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
        viewModelScope.launch(dispatcherProvider.io) {
            val sensorList = getSensors()
            sensorList.forEach {
                Log.d("register123", it.name)
                subscribeToSensorUseCase.subscribeToSensor(it.name)
            }
        }
    }

    /*fun unsubscribeSensors() {
        //Log.d("unregister123 size", sensorList.size.toString())
        viewModelScope.launch(dispatcherProvider.io)  {
            val sensorList = getSensors()
            sensorList.forEach {
                Log.d("unregister123", it.name)
                subscribeToSensorUseCase.subscribeToSensor(it.name, false)
            }
        }
    }*/

    fun subscribeToSensorData() {
        viewModelScope.launch(dispatcherProvider.io) {
            Log.d("pear2", "start")
            subscribeForSensorDataUseCase.subscribeForSensorData().collect {
                Log.d("pear2", "start")
                when (it) {
                    is Response.Success -> {
                        Log.d("pear123", it.data.toString())
                    }
                    is Response.Failure -> {
                        Log.d("pear123", it.error.toString())
                    }
                }
            }
        }
    }
}