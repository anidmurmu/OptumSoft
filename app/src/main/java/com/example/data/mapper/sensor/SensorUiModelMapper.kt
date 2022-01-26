package com.example.data.mapper.sensor

import android.util.Log
import com.example.data.entity.sensor.SensorNetworkModel
import com.example.data.entity.sensor.SensorReadingNetworkModel
import com.example.domain.mapper.Mapper
import com.example.domain.model.response.Response
import com.example.domain.model.sensor.SensorReadingUiModel
import com.example.domain.model.sensor.SensorUiModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SensorUiModelMapper @Inject constructor() :
    Mapper<Flow<Response<SensorNetworkModel>>, Flow<Response<SensorUiModel>>> {

    override fun mapFrom(inputModel: Flow<Response<SensorNetworkModel>>): Flow<Response<SensorUiModel>> =
        flow {
            inputModel.collect {
                val resp = when (it) {
                    is Response.Failure -> {
                        Response.Failure(it.error)
                    }
                    is Response.Success -> {
                        val mappedModel = mapToSensorUiModel(it.data)
                        if (mappedModel.type == "init") {
                            Log.d("mapper1", mappedModel.toString())
                            //Log.d("mapper1", it.data.toString())
                        }
                        Response.Success(mappedModel)
                    }
                }
                emit(resp)
            }
        }

    private fun mapToSensorUiModel(inputModel: SensorNetworkModel): SensorUiModel {
        return inputModel.let {
            SensorUiModel(
                it.sensorName,
                null,
                null,
                false,
                it.type,
                it.scale,
                it.sensorKey,
                it.sensorVal,
                mapToSensorReadingUiModel(it.recentList),
                mapToSensorReadingUiModel(it.minuteList),
                false
            )
        }
    }

    private fun mapToSensorReadingUiModel(
        sensorReadingNetworkModel: List<SensorReadingNetworkModel>?
    ): List<SensorReadingUiModel>? {
        return sensorReadingNetworkModel?.map {
            SensorReadingUiModel(
                it.time,
                it.reading
            )
        }
    }
}