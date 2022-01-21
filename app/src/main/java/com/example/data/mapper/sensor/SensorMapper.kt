package com.example.data.mapper.sensor

import android.util.Log
import com.example.data.entity.sensor.SensorRangeNetworkModel
import com.example.domain.mapper.Mapper
import com.example.domain.model.sensor.SensorConfigUiModel
import com.google.gson.Gson
import com.google.gson.JsonObject
import java.util.*
import javax.inject.Inject

class SensorConfigMapper @Inject constructor(
    private val gson: Gson
) : Mapper<JsonObject, Result<List<SensorConfigUiModel>>> {
    override fun mapFrom(inputModel: JsonObject): Result<List<SensorConfigUiModel>> {

        /*inputModel.onFailure {
            Log.d("pear", "failure hai")
        }
            .onSuccess {
                Log.d("pear", "success hai")
            }
        Log.d("pear4", "kay baat hai")
        return inputModel.fold(
            onFailure = {
                Log.d("pear5", "kay baat hai")
                Result.failure(it)
            },
            onSuccess = {
                Log.d("pear6", "kay baat hai")
                Result.success(mapToUiModel(it))
            }
        )*/
        return mapToUiModel(inputModel)
    }

    private fun mapToUiModel(
        jsonObject: JsonObject
    ): Result<List<SensorConfigUiModel>> {
        val strToObj = gson.fromJson(jsonObject, SortedMap::class.java)
        val keys = strToObj.keys
        val values = strToObj.values
        values.forEach {
            val model = gson.fromJson(it.toString(), SensorRangeNetworkModel::class.java)
            Log.d("pear8", model.toString())
            //Log.d("pear8", it.toString())
        }
        keys.forEach {
            Log.d("pear7", it.toString())
        }
        Log.d("pear2", strToObj.toString())

        //Log.d("pear2", gson.fromJson(jsonObject))
        val jsonStr = gson.toJson(jsonObject)
        /*val entries = sensorConfigNetworkModel.nameToSensorReading.entries
        return entries.map {
            SensorConfigUiModel(
                it.key,
                it.value.min,
                it.value.max
            )
        }*/
        Log.d("pear3", jsonStr)

        val uiModel1 = SensorConfigUiModel("", 0, 1)
        val uiModel2 = SensorConfigUiModel("", 0, 1)
        val uiModel3 = SensorConfigUiModel("", 0, 1)

        return Result.success(listOf(uiModel1, uiModel2, uiModel3))
    }
}