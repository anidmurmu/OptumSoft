package com.example.ui.sensor

import com.example.domain.model.sensor.SensorConfigUiModel

data class MainViewState(
    val hasConfigData: Boolean = false,
    val configList: List<SensorConfigUiModel> = mutableListOf()
)
