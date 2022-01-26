package com.example.domain.usecase.sensor

import com.example.domain.model.response.Response
import com.example.domain.model.sensor.SensorUiModel
import com.example.domain.repository.sensor.SensorRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface SubscribeForSensorDataUseCase {
    suspend fun subscribeForSensorData(): Flow<Response<SensorUiModel>>
}

class SubscribeForSensorDataUseCaseImpl @Inject constructor(
    private val sensorRepository: SensorRepository
) : SubscribeForSensorDataUseCase {
    override suspend fun subscribeForSensorData(): Flow<Response<SensorUiModel>> {
        return sensorRepository.subscribeForSensorData()
    }
}