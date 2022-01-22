package com.example.ui

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NetworkModel(
    @SerializedName("type")
    @Expose
    val type: String,

    @SerializedName("recent")
    @Expose
    val recentTimeUnitList: List<SensorData>?,

    @SerializedName("minute")
    @Expose
    val minuteTimeUnitList: List<SensorData>?,

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

data class SensorData(
    @SerializedName("key")
    @Expose
    val time: String?,

    @SerializedName("val")
    @Expose
    val reading: String?
)