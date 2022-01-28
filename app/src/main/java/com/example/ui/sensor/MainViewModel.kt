package com.example.ui.sensor

import android.app.Application
import android.util.Log
import androidx.lifecycle.viewModelScope
import com.example.domain.model.response.Response
import com.example.domain.model.sensor.SensorConfigUiModel
import com.example.domain.model.sensor.SensorGraphDataUiModel
import com.example.domain.model.sensor.SensorReadingUiModel
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
            /*sensorList.forEachIndexed { index, sensorConfig ->
                Log.d("register123", sensorConfig.name)
                subscribeToSensorUseCase.subscribeToSensor(sensorConfig.name)
                if (index == sensorList.size - 1) {
                    _viewState.value = _viewState.value.copy(hasSensorsSubscribed = true)
                }
            }*/

            val sensorConfig = sensorList[0]
            subscribeToSensorUseCase.subscribeToSensor(sensorConfig.name)
            _viewState.value = _viewState.value.copy(hasSensorsSubscribed = true)
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

    /*fun subscribeToSensorData() {
        viewModelScope.launch(dispatcherProvider.io) {
            val sensorList = getStoredSensors()
            var index = 0
            val sensorGraphDataUiModel = _viewState.value.sensorGraphDataUiModel
            subscribeForSensorDataUseCase.subscribeForSensorData().collect {
                when (it) {
                    is Response.Success -> {
                        Log.d("pear123", it.data.minuteList.toString())
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
    }*/

    fun subscribeToSingleSensorData() {
        viewModelScope.launch(dispatcherProvider.io) {
            val sensorList = getStoredSensors()
            val storedSensorUiModel = sensorList[0]
            val sensorGraphDataUiModel = _viewState.value.sensorGraphDataUiModel
            subscribeForSensorDataUseCase.subscribeForSensorData().collect {
                when (it) {
                    is Response.Success -> {
                        Log.d("data1", it.data.toString())
                        val name = storedSensorUiModel.name
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
        Log.d("inside1", "handle")
        when {
            type.equals("init", true) -> {
                Log.d("inside1", "init")
                handleTypeInit(sortedMap, sensorName, sensorUiModel)
                Log.d("rabbit", sensorGraphDataUiModel.sortedMap.toString())
                initGraph(sortedMap, sensorName)
            }
            type.equals("update", true) -> {
                Log.d("inside1", "update")
                handleTypeUpdate(sortedMap, sensorUiModel)
                updateGraph(sortedMap, sensorName)
            }
            type.equals("delete", true) -> {
                Log.d("inside1", "delete")
                handleTypeDelete(sortedMap, sensorUiModel)
                updateGraph(sortedMap, sensorName)
            }
        }

        return sensorGraphDataUiModel
    }

    private fun handleTypeInit(
        sortedMap: SortedMap<String, SensorUiModel>,
        name: String,
        sensorUiModel: SensorUiModel
    ) {
        val uiModel = sensorUiModel.copy(name = name)
        val inserted = sortedMap.putIfAbsent(name, uiModel)
        Log.d("inside1 insert", inserted.toString())
        _viewState.value = _viewState.value.copy(valueInserted = true)
    }

    private fun handleTypeUpdate(
        sortedMap: SortedMap<String, SensorUiModel>,
        sensorUiModel: SensorUiModel
    ) {
        val storedSensorUiModel = sortedMap[sensorUiModel.name]
        val dataList = if (sensorUiModel.scale == "recent") {
            storedSensorUiModel?.recentList
        } else {
            storedSensorUiModel?.minuteList
        }
        var isValUpdate = false
        var newModel: SensorReadingUiModel? = null
        var idx = 0
        synchronized(this) {
            dataList?.forEachIndexed { index, sensorReadingUiModel ->
                if (sensorReadingUiModel.sensorKey.equals(sensorUiModel.sensorKey)) {
                    Log.d("value12", sensorReadingUiModel.sensorVal.toString())
                    Log.d("value12", sensorUiModel.sensorVal.toString())
                    newModel = SensorReadingUiModel(
                        sensorReadingUiModel.sensorKey,
                        sensorReadingUiModel.sensorKey
                    )
                    //Log.d("update1 before", dataList[index].toString())
                    //dataList[index] = newModel
                    idx = index
                    isValUpdate = true
                    //Log.d("update1 after", dataList[index].toString())
                }
            }

            newModel?.let {
                dataList?.add(idx, it)
            }
            if (!isValUpdate) {
                val readingModel = SensorReadingUiModel(
                    sensorUiModel.sensorKey,
                    sensorUiModel.sensorVal
                )
                dataList?.add(readingModel)
            }
        }
    }

    private fun handleTypeDelete(
        sortedMap: SortedMap<String, SensorUiModel>,
        sensorUiModel: SensorUiModel
    ) {
        val storedSensorUiModel = sortedMap[sensorUiModel.name]
        val dataList = if (sensorUiModel.scale == "recent") {
            storedSensorUiModel?.recentList
        } else {
            storedSensorUiModel?.minuteList
        }
        var idx = -1
        synchronized(this) {
            dataList?.forEachIndexed { index, sensorReadingUiModel ->
                if (sensorReadingUiModel.sensorKey.equals(sensorUiModel.sensorKey)) {
                    //dataList.removeAt(index)
                    //return
                    idx = index
                }
            }

            if (idx >= 0) {
                dataList?.removeAt(idx)
            }
        }
    }

    fun showGraphList(sortedMap: SortedMap<String, SensorUiModel>, name: String) {
        Log.d("list1 recent", sortedMap[name]?.recentList.toString())
        Log.d("list1 minute", sortedMap[name]?.minuteList.toString())
    }

    fun initGraph(
        sortedMap: SortedMap<String, SensorUiModel>,
        sensorName: String,
        scale: String = "recent"
    ): List<SensorReadingUiModel>? {
        val sensorUiModel = sortedMap[sensorName]
        val dataList = if (scale == "recent") {
            sensorUiModel?.recentList
        } else {
            sensorUiModel?.minuteList
        }
        return dataList
    }

    fun updateGraph(
        sortedMap: SortedMap<String, SensorUiModel>,
        sensorName: String,
        scale: String = "recent"
    ) {

    }
}