package com.example.domain.usecase.sensor

import com.example.domain.model.sensor.SensorUiModel
import com.example.domain.repository.sensor.SensorRepository
import javax.inject.Inject

interface GetSensorListUseCase {
    suspend fun getSensorList(): Result<List<SensorUiModel>>
}

class GetSensorListUseCaseImpl @Inject constructor(
    private val sensorRepository: SensorRepository
) : GetSensorListUseCase {
    override suspend fun getSensorList(): Result<List<SensorUiModel>> {
        return sensorRepository.getSensorList()
    }
}