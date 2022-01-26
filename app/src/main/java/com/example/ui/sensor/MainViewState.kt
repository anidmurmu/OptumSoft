package com.example.ui.sensor

import androidx.lifecycle.MutableLiveData
import com.example.domain.model.sensor.SensorConfigUiModel
import com.example.domain.model.sensor.SensorGraphDataUiModel
import com.example.ui.utils.base.recyclerview.BaseBindingRVModel

data class MainViewState(
    val hasConfigData: Boolean = false,
    val configList: List<SensorConfigUiModel> = mutableListOf(),
    val hasSensorsSubscribed: Boolean = false,
    val sensorNameList: MutableLiveData<List<BaseBindingRVModel>> = MutableLiveData(emptyList()),
    val sensorGraphDataUiModel: SensorGraphDataUiModel = SensorGraphDataUiModel()
)
