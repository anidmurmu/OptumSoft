package com.example.data.repositoryimpl.sensor

import com.example.data.mapper.sensor.SensorConfigMapper
import com.example.data.source.network.retrofit.sensor.SensorService
import com.example.domain.model.sensor.SensorConfigUiModel
import com.example.domain.repository.sensor.SensorRepository
import javax.inject.Inject

class SensorRepositoryImpl @Inject constructor(
    private val sensorService: SensorService,
    private val sensorConfigMapper: SensorConfigMapper
) : SensorRepository {
    override suspend fun getSensorList(): Result<List<String>> {
        val response = try {
            return Result.success(sensorService.getSensorList())
        } catch (ex: Exception) {
            Result.failure<List<String>>(ex)
        }
        return response
    }

    override suspend fun getSensorConfigList(): Result<List<SensorConfigUiModel>> {
        val response = try {
            val resp = Result.success(sensorService.getSensorConfigList())
            return sensorConfigMapper.mapFrom(resp)
        } catch (ex: Exception) {
            Result.failure<List<SensorConfigUiModel>>(ex)
        }
        return response
    }
}