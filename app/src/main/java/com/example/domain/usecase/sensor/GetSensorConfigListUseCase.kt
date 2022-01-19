package com.example.domain.usecase.sensor

import com.example.domain.model.sensor.SensorUiModel
import com.example.domain.repository.sensor.SensorRepository
import javax.inject.Inject

interface GetSensorConfigListUseCase {
    suspend fun getSensorConfigList(): Result<List<SensorUiModel>>
}

class GetSensorConfigListUseCaseImpl @Inject constructor(
    private val sensorRepository: SensorRepository
) : GetSensorConfigListUseCase {
    override suspend fun getSensorConfigList(): Result<List<SensorUiModel>> {
        return sensorRepository.getSensorConfigList()
    }
}