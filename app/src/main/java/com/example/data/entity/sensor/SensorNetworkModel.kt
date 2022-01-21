package com.example.data.entity.sensor

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SensorNetworkModel(
    @SerializedName("type")
    @Expose
    val type: String,

    @SerializedName("recent")
    @Expose
    val recentTimeUnitList: List<SensorReadingNetworkModel>?,

    @SerializedName("minute")
    @Expose
    val minuteTimeUnitList: List<SensorReadingNetworkModel>?,

    @SerializedName("key")
    @Expose
    val sensorTimeUnit: String?,

    @SerializedName("val")
    @Expose
    val sensorReading: String?,

    @SerializedName("sensor")
    @Expose
    val sensorName: String?,

    @SerializedName("scale")
    @Expose
    val scale: String?,
)

data class SensorReadingNetworkModel(
    @SerializedName("key")
    @Expose
    val time: String?,

    @SerializedName("val")
    @Expose
    val reading: String?
)

data class SensorConfigNetworkModel(
    val nameToSensorReading: Map<String, SensorRangeNetworkModel>
)

data class SensorRangeNetworkModel(
    @SerializedName("min")
    @Expose
    val min: Int,

    @SerializedName("max")
    @Expose
    val max: Int
)