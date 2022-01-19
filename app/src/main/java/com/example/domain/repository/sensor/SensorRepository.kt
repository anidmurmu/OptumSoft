package com.example.domain.repository.sensor

import com.example.domain.model.sensor.SensorUiModel

interface SensorRepository {
    suspend fun getSensorList(): Result<List<SensorUiModel>>
    suspend fun getSensorConfigList(): Result<List<SensorUiModel>>
}