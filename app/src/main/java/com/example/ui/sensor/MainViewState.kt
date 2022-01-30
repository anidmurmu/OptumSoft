package com.example.ui.sensor

import androidx.lifecycle.MutableLiveData
import com.example.domain.model.sensor.SensorConfigUiModel
import com.example.domain.model.sensor.SensorGraphDataUiModel
import com.example.ui.utils.base.recyclerview.BaseBindingRVModel

data class MainViewState(
    val hasSensorConfigList: Boolean = false,
    val sensorConfigList: List<SensorConfigUiModel> = mutableListOf(),
    val isSensorSubscribed: Boolean = false,
    val rvSensorNameList: MutableLiveData<List<BaseBindingRVModel>> = MutableLiveData(emptyList()),
    val sensorGraphDataUiModel: SensorGraphDataUiModel = SensorGraphDataUiModel(),
    val valueInserted: Boolean = false,
    val isScaleTypeRecent: Boolean = true,
    val isSensorListShowing: Boolean = false,
    val currentSubscribedSensor: String = "",
    val state: State = State.Initial,
    val toastMsg: MutableLiveData<String> = MutableLiveData("")
)

sealed class State {
    object Initial : State()
    object SensorConfigListShowing : State()
    object SensorSubscribed : State()
    object SensorUnsubscribed : State()
    object OnSubscriptionChange : State()
    object UpdateGraph: State()
}
