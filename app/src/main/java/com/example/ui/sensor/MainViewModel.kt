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
import com.example.optumsoft.R
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

    val TAG = "MainViewModel"

    private val _viewState: MutableStateFlow<MainViewState> =
        MutableStateFlow(MainViewState())
    val viewState: StateFlow<MainViewState> = _viewState

    private val _graphUpdate: MutableStateFlow<SensorUiModel> = MutableStateFlow(getSensorUiModel())
    val graphUpdate: StateFlow<SensorUiModel> = _graphUpdate


    fun getSensorNameList() {
        viewModelScope.launch(dispatcherProvider.io) {
            getSensorListUseCase.getSensorList()
                .onFailure {
                    Log.e(TAG, (it as Exception).toString())
                }
                .onSuccess {
                }
        }
    }

    fun getSensorConfigList() {
        viewModelScope.launch(dispatcherProvider.io) {
            when (val result = getSensorConfigListUseCase.getSensorConfigList()) {
                is Response.Failure -> {
                    Log.e(TAG, result.error.message.toString())
                }
                is Response.Success -> {
                    handleSensorConfigSuccess(result.data)
                }

            }
        }
    }

    private fun handleSensorConfigSuccess(sensorConfigList: List<SensorConfigUiModel>) {
        val sensorConfigUiModel = sensorConfigList[0]
        val modelList = toSensorUiModelList(sensorConfigList, getSensorUiModel(sensorConfigUiModel))
        val viewableList = toSensorViewableList(modelList)
        _viewState.value.rvSensorNameList.postValue(viewableList)
        _viewState.value = _viewState.value.copy(
            sensorConfigList = sensorConfigList,
            state = State.SensorConfigListShowing
        )
    }

    private fun getSensorUiModel(sensorConfigUiModel: SensorConfigUiModel? = null): SensorUiModel {
        return SensorUiModel(
            sensorConfigUiModel?.name,
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
    }

    fun getGraphData(sensorName: String): List<SensorReadingUiModel>? {
        val sensorUiModel = _viewState.value.sensorGraphDataUiModel.sortedMap[sensorName]
        val isScaleTypeRecent = _viewState.value.isScaleTypeRecent
        val dataList = if (isScaleTypeRecent) {
            sensorUiModel?.recentList
        } else {
            sensorUiModel?.minuteList
        }
        return dataList?.toList()
    }

    private fun isDeviated(sensorName: String): Boolean {
        val sensorUiModel = _viewState.value.sensorGraphDataUiModel.sortedMap[sensorName]
        val isScaleTypeRecent = _viewState.value.isScaleTypeRecent
        val dataList = if (isScaleTypeRecent) {
            sensorUiModel?.recentList
        } else {
            sensorUiModel?.minuteList
        }

        var isDeviated = false
        val sensorConfigList = _viewState.value.sensorConfigList
        var sensorConfigUiModel: SensorConfigUiModel? = null
        sensorConfigList.forEach {
            if (it.name.equals(sensorName, true)) {
                sensorConfigUiModel = it
            }
        }
        sensorConfigUiModel?.let { sensorConfig ->
            dataList?.forEach { sensorReading ->
                sensorReading.sensorVal?.let {
                    if (it.toFloat().toInt() >= sensorConfig.min
                        && it.toFloat().toInt() <= sensorConfig.max
                    ) {
                        isDeviated = true
                    }
                }
            }
        }
        return isDeviated
    }

    fun getDeviationStr(sensorName: String): String {
        val isDeviated = isDeviated(sensorName)
        return if (isDeviated) {
            "Deviation : " + "Yes"
        } else {
            "Deviation : " + "No"
        }
    }

    fun getFirstSensorName(): String {
        return getStoredSensorConfigList()[0].name
    }

    fun getSubscribedSensorName(): String {
        return _viewState.value.currentSubscribedSensor
    }

    private fun getStoredSensorConfigList(): List<SensorConfigUiModel> {
        return _viewState.value.sensorConfigList
    }

    fun subscribeToSensor(sensorName: String) {
        viewModelScope.launch(dispatcherProvider.io) {
            subscribeToSensorUseCase.subscribeToSensor(sensorName)
            _viewState.value = _viewState.value.copy(
                currentSubscribedSensor = sensorName,
                state = State.SensorSubscribed
            )
        }
    }

    private fun unsubscribeFromSensor(sensorName: String) {
        viewModelScope.launch(dispatcherProvider.io) {
            subscribeToSensorUseCase.subscribeToSensor(sensorName, false)
        }
        _viewState.value = _viewState.value.copy(
            currentSubscribedSensor = "",
            state = State.SensorUnsubscribed
        )
    }

    fun subscribeToSensorData() {
        viewModelScope.launch(dispatcherProvider.io) {
            val sensorGraphDataUiModel = _viewState.value.sensorGraphDataUiModel
            subscribeForSensorDataUseCase.subscribeForSensorData().collect {
                when (it) {
                    is Response.Success -> {
                        Log.d(TAG, it.data.toString())
                        val subscribedName = getSubscribedSensorName()
                        handleSubscribedData(it.data, sensorGraphDataUiModel, subscribedName)
                    }
                    is Response.Failure -> {
                        Log.d(TAG, it.error.toString())
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
                handleTypeInit(sortedMap, sensorName, sensorUiModel)
            }
            type.equals("update", true) -> {
                handleTypeUpdate(sortedMap, sensorUiModel)
                _graphUpdate.value = sensorUiModel
            }
            type.equals("delete", true) -> {
                handleTypeDelete(sortedMap, sensorUiModel)
                _graphUpdate.value = sensorUiModel
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
        sortedMap.putIfAbsent(name, uiModel)
        _viewState.value = _viewState.value.copy(state = State.UpdateGraph)
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
                    newModel = SensorReadingUiModel(
                        sensorReadingUiModel.sensorKey,
                        sensorReadingUiModel.sensorKey
                    )
                    idx = index
                    isValUpdate = true
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
                    idx = index
                }
            }

            if (idx >= 0) {
                dataList?.removeAt(idx)
            }
        }
    }

    fun setScaleTypeToRecent() {
        _viewState.value = _viewState.value.copy(isScaleTypeRecent = true)
    }

    fun setScaleTypeToMinute() {
        _viewState.value = _viewState.value.copy(isScaleTypeRecent = false)
    }

    fun showToast(msg: String) {
        _viewState.value.toastMsg.postValue(msg)
    }

    private fun handleSensorItemClick(sensorUiModel: SensorUiModel) {
        updateSelectedItem(sensorUiModel)
        updateSensorSubscription(sensorUiModel)
    }

    private fun updateSensorSubscription(sensorUiModel: SensorUiModel) {
        val subscribedSensorName = getSubscribedSensorName()
        unsubscribeFromSensor(subscribedSensorName)
        subscribeToSensor(sensorUiModel.name ?: "")
        _viewState.value = _viewState.value.copy(
            currentSubscribedSensor = sensorUiModel.name ?: "",
            state = State.OnSubscriptionChange
        )
    }

    private fun updateSelectedItem(sensorUiModel: SensorUiModel) {
        val sensorList = getStoredSensorConfigList()
        val sensorUiModelList = toSensorUiModelList(sensorList, sensorUiModel)
        val viewableList = toSensorViewableList(sensorUiModelList)
        _viewState.value.rvSensorNameList.postValue(viewableList)
    }

    private fun toSensorViewableList(
        sensorConfigList: List<SensorUiModel>
    ): List<BaseBindingRVModel> {
        return sensorConfigList.map {
            SensorRVModel(it)
        }
    }

    private fun toSensorUiModelList(
        sensorConfigList: List<SensorConfigUiModel>,
        sensorUiModel: SensorUiModel
    ): List<SensorUiModel> {
        return sensorConfigList.map {
            var model = getSensorUiModel(it)
            if (sensorUiModel.name.equals(it.name, true)) {
                model = model.copy(color = "#BEBDBB")
            }
            model
        }
    }

    override fun onViewClick(id: Int, data: Any) {
        when (id) {
            R.id.on_click_sensor_item -> {
                val sensorUiModel = data as SensorUiModel
                handleSensorItemClick(sensorUiModel)
            }
        }
    }
}