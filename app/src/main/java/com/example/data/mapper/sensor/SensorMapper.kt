package com.example.data.mapper.sensor

import android.util.Log
import com.example.data.entity.sensor.SensorRangeNetworkModel
import com.example.domain.mapper.Mapper
import com.example.domain.model.response.Response
import com.example.domain.model.sensor.SensorConfigUiModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.util.*
import javax.inject.Inject

class SensorConfigMapper @Inject constructor(
    private val gson: Gson
) : Mapper<JsonObject, Response<List<SensorConfigUiModel>>> {
    override fun mapFrom(inputModel: JsonObject): Response<List<SensorConfigUiModel>> {

        val result = try {
            val sortedMap: SortedMap<String, SensorRangeNetworkModel> =
                sortedMapToSensorRangeNetworkModel(inputModel)
            val sensorConfigUiModelList = toUiModelList(sortedMap)
            Response.Success(sensorConfigUiModelList)
        } catch (ex: Exception) {
            Response.Failure(ex)
        }
        return result
    }

    private fun sortedMapToSensorRangeNetworkModel(jsonObject: JsonObject)
            : SortedMap<String, SensorRangeNetworkModel> {
        val sensorConfigNetworkModelMap: SortedMap<String, SensorRangeNetworkModel> =
            sortedMapOf()

        val jsonToSortedMap = gson.fromJson(jsonObject, SortedMap::class.java)
        jsonToSortedMap.forEach { entry ->
            val name = entry.key.toString()
            val model = gson.fromJson(
                entry.value.toString(),
                SensorRangeNetworkModel::class.java
            )
            sensorConfigNetworkModelMap[name] = model
        }
        return sensorConfigNetworkModelMap
    }

    private fun toUiModelList(sortedMap: SortedMap<String, SensorRangeNetworkModel>)
            : List<SensorConfigUiModel> {
        return sortedMap.map {
            SensorConfigUiModel(
                it.key,
                it.value.min,
                it.value.max
            )
        }
    }
}