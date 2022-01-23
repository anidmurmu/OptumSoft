package com.example.ui.sensor

import com.example.domain.model.sensor.SensorConfigUiModel
import kotlinx.coroutines.flow.MutableStateFlow

sealed class MainViewState {
    val sensorConfigs: MutableStateFlow<List<SensorConfigUiModel>> = MutableStateFlow(emptyList())

    object Initial : MainViewState()
    data class HasConfigList(val sensorConfigList: List<SensorConfigUiModel>) : MainViewState()
    object Failure : MainViewState()
}
