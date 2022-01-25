package com.example.ui.sensor

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class NetworkModel(
    @SerializedName("type")
    @Expose
    val type: String,

    @SerializedName("recent")
    @Expose
    val recentList: List<SensorData>?,

    @SerializedName("minute")
    @Expose
    val minuteList: List<SensorData>?,

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