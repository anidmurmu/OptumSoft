package com.example.domain.model.sensor

data class SensorUiModel(
    val name: String,
    val minReading: Int?,
    val maxReading: Int?,
    val isDeviated: Boolean,
    val type: String?,
    val recentList: List<SensorReadingUiModel>?,
    val minuteList: List<SensorReadingUiModel>?,
    val isSelected: Boolean = false
)

data class SensorReadingUiModel(
    val time: String?,
    val reading: String?
)
