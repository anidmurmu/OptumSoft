package com.example.domain.repository.sensor

import com.example.domain.model.response.Response
import com.example.domain.model.sensor.SensorConfigUiModel
import com.example.domain.model.sensor.SensorUiModel
import kotlinx.coroutines.flow.Flow

interface SensorRepository {
    suspend fun getSensorList(): Result<List<String>>
    suspend fun getSensorConfigList(): Response<List<SensorConfigUiModel>>
    suspend fun subscribeForSensorData(): Flow<Response<SensorUiModel>>
}