package com.example.data.mapper.sensor

import com.example.data.entity.sensor.SensorConfigNetworkModel
import com.example.data.entity.sensor.SensorReadingNetworkModel
import com.example.domain.mapper.Mapper
import com.example.domain.model.sensor.SensorConfigUiModel
import com.example.domain.model.sensor.SensorReadingUiModel
import javax.inject.Inject

class SensorConfigMapper @Inject constructor() :
    Mapper<Result<List<SensorConfigNetworkModel>>, Result<List<SensorConfigUiModel>>> {
    override fun mapFrom(inputModel: Result<List<SensorConfigNetworkModel>>): Result<List<SensorConfigUiModel>> {
        return inputModel.fold(
            onFailure = {
                Result.failure(it)
            },
            onSuccess = {
                Result.success(mapToUiModel(it))
            }
        )
    }

    private fun mapToUiModel(networkModelList: List<SensorConfigNetworkModel>): List<SensorConfigUiModel> {
        return networkModelList.map {
            SensorConfigUiModel(
                it.name,
                it.time?.toInt() ?: 0,
                it.reading?.toInt() ?: 0
            )
        }
    }

    private fun mapToSensorReadingUiModel(sensorReadingList: List<SensorReadingNetworkModel>?)
            : List<SensorReadingUiModel>? {
        return sensorReadingList?.map {
            SensorReadingUiModel(
                it.time,
                it.reading
            )
        }
    }
}