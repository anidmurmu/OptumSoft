package com.example.ui.sensor

import androidx.lifecycle.MutableLiveData
import com.example.domain.model.sensor.SensorConfigUiModel
import com.example.ui.utils.base.recyclerview.BaseBindingRVModel

data class MainViewState(
    val hasConfigData: Boolean = false,
    val configList: List<SensorConfigUiModel> = mutableListOf(),
    val sensorNameList: MutableLiveData<List<BaseBindingRVModel>> = MutableLiveData(emptyList())
)
