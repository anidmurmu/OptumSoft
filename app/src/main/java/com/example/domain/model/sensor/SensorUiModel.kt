package com.example.domain.model.sensor

import java.util.*

data class SensorUiModel(
    val name: String?,
    val minReading: Int?,
    val maxReading: Int?,
    val isDeviated: Boolean,
    val type: String,
    val scale: String?,
    val sensorKey: String?,
    val sensorVal: String?,
    val recentList: List<SensorReadingUiModel>?,
    val minuteList: List<SensorReadingUiModel>?,
    val isSelected: Boolean = false
)

data class SensorGraphDataUiModel(
    val sortedMap: SortedMap<String, SensorUiModel> = sortedMapOf()
)

data class SensorReadingUiModel(
    val time: String?,
    val reading: String?
)

data class SensorConfigUiModel(
    val name: String,
    val min: Int,
    val max: Int
)
