package com.example.domain.usecase.sensor

import com.example.domain.model.response.Response
import com.example.domain.model.sensor.SensorConfigUiModel
import com.example.domain.repository.sensor.SensorRepository
import javax.inject.Inject

interface GetSensorConfigListUseCase {
    suspend fun getSensorConfigList(): Response<List<SensorConfigUiModel>>
}

class GetSensorConfigListUseCaseImpl @Inject constructor(
    private val sensorRepository: SensorRepository
) : GetSensorConfigListUseCase {
    override suspend fun getSensorConfigList(): Response<List<SensorConfigUiModel>> {
        return sensorRepository.getSensorConfigList()
    }
}