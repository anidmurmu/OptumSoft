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
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
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

    private val _graphUpdate: MutableStateFlow<SensorUiModel> = MutableStateFlow(getSensorUiModel())
    val graphUpdate: StateFlow<SensorUiModel> = _graphUpdate

    private fun getSensorUiModel(): SensorUiModel {
       return SensorUiModel(
            "",
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


    fun getSensorNameList() {
        viewModelScope.launch(dispatcherProvider.io) {
            getSensorListUseCase.getSensorList()
                .onFailure {
                    Log.e("apple1", (it as Exception).toString())
                }
                .onSuccess {
                    Log.d("apple2", it.toString())
                    /*val modelList = namesToViewableList(it)
                    _viewState.value.rvSensorNameList.postValue(modelList)
                    _viewState.value = _viewState.value.copy(
                        isSensorListShowing = true
                    )*/
                }
        }
    }

    private fun namesToViewableList(nameList: List<String>): List<BaseBindingRVModel> {
        return nameList.mapIndexed { index, name ->
            var sensorModel = SensorUiModel(
                name,
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
            if (index == 0) {
                sensorModel = sensorModel.copy(color = "#BEBDBB")
            }
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
                    handleSensorConfigSuccess(result.data)
                }

            }
        }
    }

    private fun handleSensorConfigSuccess(sensorConfigList: List<SensorConfigUiModel>) {
        val sensorConfigUiModel = sensorConfigList[0]
        val modelList = toSensorUiModelList(sensorConfigList, toSensorUiModel(sensorConfigUiModel))
        val viewableList = toSensorViewableList(modelList)
        _viewState.value.rvSensorNameList.postValue(viewableList)
        _viewState.value = _viewState.value.copy(
            sensorConfigList = sensorConfigList,
            state = State.SensorConfigListShowing
        )
    }

    private fun toSensorUiModel(sensorConfigUiModel: SensorConfigUiModel): SensorUiModel {
        return SensorUiModel(
            sensorConfigUiModel.name,
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

    fun subscribeToSensorInitially() {
        val sensorName = getFirstSensorName()
        //subscribeToSensorData(sensorName)
        subscribeToSensor(sensorName)
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
            Log.d("before1", sensorName)
            subscribeToSensorUseCase.subscribeToSensor(sensorName)
            _viewState.value = _viewState.value.copy(
                currentSubscribedSensor = sensorName,
                state = State.SensorSubscribed
            )
        }
    }

    private fun unsubscribeFromSensor(sensorName: String) {
        viewModelScope.launch(dispatcherProvider.io) {
            Log.d("unsub124", sensorName)
            subscribeToSensorUseCase.subscribeToSensor(sensorName, false)
        }
        _viewState.value = _viewState.value.copy(
            currentSubscribedSensor = "",
            state = State.SensorUnsubscribed
        )
    }

    /*fun subscribeToSensorData(sensorName: String) {
        viewModelScope.launch(dispatcherProvider.io) {
            val sensorGraphDataUiModel = _viewState.value.sensorGraphDataUiModel
            subscribeForSensorDataUseCase.subscribeForSensorData().collect {
                when (it) {
                    is Response.Success -> {
                        Log.d("data1", it.data.toString())
                        val graphDataUiModel =
                            handleSubscribedData(it.data, sensorGraphDataUiModel, sensorName)
                    }
                    is Response.Failure -> {
                        Log.d("pear123", it.error.toString())
                    }
                }
            }
        }
    }*/

    fun subscribeToSensorData1() {
        viewModelScope.launch(dispatcherProvider.io) {
            val sensorGraphDataUiModel = _viewState.value.sensorGraphDataUiModel
            subscribeForSensorDataUseCase.subscribeForSensorData().collect {
                when (it) {
                    is Response.Success -> {
                        Log.d("data1", it.data.toString())
                        val subscribedName = getSubscribedSensorName()
                        val graphDataUiModel =
                            handleSubscribedData(it.data, sensorGraphDataUiModel, subscribedName)
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
                //Log.d("init123", "init")
                Log.d("init1234 name", sensorName)
                handleTypeInit(sortedMap, sensorName, sensorUiModel)
                Log.d("rabbit", sensorGraphDataUiModel.sortedMap.toString())
            }
            type.equals("update", true) -> {
                Log.d("update123", "update")
                handleTypeUpdate(sortedMap, sensorUiModel)
                onGraphUpdate()
                _graphUpdate.value = sensorUiModel
            }
            type.equals("delete", true) -> {
                Log.d("delete123", "delete")
                handleTypeDelete(sortedMap, sensorUiModel)
                onGraphUpdate()
                _graphUpdate.value = sensorUiModel
            }
        }

        return sensorGraphDataUiModel
    }

    fun getIsScaleTypeRecent(): Boolean {
        return _viewState.value.isScaleTypeRecent
    }

    private fun handleTypeInit(
        sortedMap: SortedMap<String, SensorUiModel>,
        name: String,
        sensorUiModel: SensorUiModel
    ) {
        val uiModel = sensorUiModel.copy(name = name)
        Log.d("handleinit123", name)
        val inserted = sortedMap.putIfAbsent(name, uiModel)
        Log.d("handleinit123 insert", inserted.toString())
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

    fun onGraphUpdate(): Flow<Unit> = flow {
        emit(Unit)
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
        onSubscriptionUpdate(sensorUiModel)
    }

    private fun updateSensorSubscription(sensorUiModel: SensorUiModel) {
        val subscribedSensorName = getSubscribedSensorName()
        Log.d("unsub123 prev", subscribedSensorName)
        Log.d("unsub123 curr", sensorUiModel.name ?: "")
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

    private fun onSubscriptionUpdate(sensorUiModel: SensorUiModel) {
        // listen to data
        //subscribeToSensorData1()
        // update graph
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
            var model = SensorUiModel(
                it.name,
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