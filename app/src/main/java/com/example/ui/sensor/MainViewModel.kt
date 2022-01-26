package com.example.ui.sensor

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.domain.model.response.Response
import com.example.domain.model.sensor.SensorConfigUiModel
import com.example.domain.model.sensor.SensorGraphDataUiModel
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
import java.util.*
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
                null,
                null,
                null,
                null
            )
            SensorRVModel(sensorModel)
        }
    }

    fun getSensorConfigList() {
        viewModelScope.launch(dispatcherProvider.io) {
            when (val result = getSensorConfigListUseCase.getSensorConfigList()) {
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

    private fun getStoredSensors(): List<SensorConfigUiModel> {
        return _viewState.value.configList
    }

    fun subscribeSensors() {
        viewModelScope.launch(dispatcherProvider.io) {
            val sensorList = getStoredSensors()
            sensorList.forEachIndexed { index, sensorConfig ->
                Log.d("register123", sensorConfig.name)
                subscribeToSensorUseCase.subscribeToSensor(sensorConfig.name)
                if (index == sensorList.size - 1) {
                    _viewState.value = _viewState.value.copy(hasSensorsSubscribed = true)
                }
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
            val sensorList = getStoredSensors()
            var index = 0
            val sensorGraphDataUiModel = _viewState.value.sensorGraphDataUiModel
            subscribeForSensorDataUseCase.subscribeForSensorData().collect {
                when (it) {
                    is Response.Success -> {
                        Log.d("pear123", it.data.toString())
                        val sensorUiModel = it.data
                        val name = if (it.data.type.equals("init", true)) {
                            if (index < sensorList.size) {
                                val name = sensorList[index].name
                                index++
                                name
                            } else {
                                ""
                            }
                        } else {
                            ""
                        }
                        val graphDataUiModel =
                            handleSubscribedData(it.data, sensorGraphDataUiModel, name)
                    }
                    is Response.Failure -> {
                        Log.d("pear123", it.error.toString())
                    }
                }
            }
        }
    }

    private fun handleSubscribedData(
        sensorUiModel: SensorUiModel,
        sensorGraphDataUiModel: SensorGraphDataUiModel,
        sensorName: String
    ): SensorGraphDataUiModel {
        val type = sensorUiModel.type
        val sortedMap = sensorGraphDataUiModel.sortedMap

        when {
            type.equals("init", true) -> {
                if (sensorName.isNotEmpty()) {
                    handleTypeInit(sortedMap, sensorName, sensorUiModel)
                    Log.d("rabbit", sensorGraphDataUiModel.sortedMap.toString())
                }
            }
            type.equals("update", true) -> {

            }
            type.equals("delete", true) -> {

            }
        }

        return sensorGraphDataUiModel
    }

    private fun handleTypeInit(
        sortedMap: SortedMap<String, SensorUiModel>,
        name: String,
        sensorUiModel: SensorUiModel
    ) {
        sortedMap.putIfAbsent(name, sensorUiModel)
    }

    private fun handleTypeUpdate(
        sortedMap: SortedMap<String, SensorUiModel>,
        name: String,
        sensorUiModel: SensorUiModel
    ) {

    }

    private fun handleTypeDelete(
        sortedMap: SortedMap<String, SensorUiModel>,
        name: String,
        sensorUiModel: SensorUiModel
    ) {

    }
}