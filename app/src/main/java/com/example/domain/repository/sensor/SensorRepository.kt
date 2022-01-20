package com.example.domain.repository.sensor

import com.example.domain.model.sensor.SensorConfigUiModel

interface SensorRepository {
    suspend fun getSensorList(): Result<List<String>>
    suspend fun getSensorConfigList(): Result<List<SensorConfigUiModel>>
}